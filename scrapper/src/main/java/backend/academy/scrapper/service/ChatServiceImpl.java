package backend.academy.scrapper.service;

import backend.academy.scrapper.exceptions.DoubleRegistrationException;
import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.models.LinkInfo;
import backend.academy.scrapper.repository.database.LinksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    @Autowired
    private LinksRepository linksRepository;

    @Override
    public void register(long chatId) {
        List<LinkInfo> linksById = linksRepository.findById(chatId);

        if (linksById.isEmpty()){
            throw new DoubleRegistrationException();
        }

        linksRepository.register(chatId);
    }

    @Override
    public void unRegister(long chatId) {
        List<LinkInfo> linksById = linksRepository.findById(chatId);

        if (linksById.isEmpty()){
            throw new NotExistTgChatException();
        }
        linksRepository.unRegister(chatId);
    }
}
