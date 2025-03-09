package backend.academy.scrapper.service;

import backend.academy.scrapper.exceptions.DoubleRegistrationException;
import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.repository.database.LinksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final LinksRepository linksRepository;

    @Override
    public void register(long chatId) {

        if (linksRepository.isRegistered(chatId)) {
            throw new DoubleRegistrationException();
        }

        linksRepository.register(chatId);
    }

    @Override
    public void unRegister(long chatId) {
        if (!linksRepository.isRegistered(chatId)) {
            throw new NotExistTgChatException();
        }
        linksRepository.unRegister(chatId);
    }
}
