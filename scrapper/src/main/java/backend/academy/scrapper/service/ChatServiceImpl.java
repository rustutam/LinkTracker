package backend.academy.scrapper.service;

import backend.academy.scrapper.exceptions.DoubleRegistrationException;
import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.repository.LinksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private LinksRepository linksRepository;

    @Override
    public void register(long chatId) {
        var optionalLinks =  linksRepository.findById(chatId);

        if (optionalLinks.isPresent()) {
            throw new DoubleRegistrationException();
        } else {
            linksRepository.register(chatId);
        }

    }

    @Override
    public void unRegister(long chatId) {
        var optionalLinks =  linksRepository.findById(chatId);

        if (optionalLinks.isEmpty()) {
            throw new NotExistTgChatException();
        }
        else {
            linksRepository.unRegister(chatId);
        }
    }
}
