package backend.academy.scrapper.service.jdbc;

import backend.academy.scrapper.exceptions.DoubleRegistrationException;
import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.repository.database.ChatRepository;
import backend.academy.scrapper.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcChatService implements ChatService {
    private final ChatRepository chatRepository;

    @Override
    public void register(ChatId chatId) {
        try {
            chatRepository.save(chatId);
        } catch (DoubleRegistrationException e){
            throw new DoubleRegistrationException();
        }
    }

    @Override
    public void unRegister(ChatId chatId) {
        try {
            chatRepository.deleteByChatId(chatId);
        } catch (NotExistTgChatException e){
            throw new NotExistTgChatException();
        }
    }
}
