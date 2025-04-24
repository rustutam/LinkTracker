package backend.academy.scrapper.service.jdbc;

import backend.academy.scrapper.exceptions.DoubleRegistrationException;
import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.repository.database.UserRepository;
import backend.academy.scrapper.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcChatService implements ChatService {
    private final UserRepository userRepository;

    @Override
    public void register(ChatId chatId) {
        try {
            userRepository.save(chatId);
        } catch (DoubleRegistrationException e){
            log.atError()
                .addKeyValue("chatId", chatId)
                .setMessage("Пользователь с таким chatId уже зарегистрирован")
                .log();
            throw new DoubleRegistrationException();
        }
    }

    @Override
    public void unRegister(ChatId chatId) {
        try {
            userRepository.deleteByChatId(chatId);
        } catch (NotExistTgChatException e){
            throw new NotExistTgChatException();
        }
    }
}
