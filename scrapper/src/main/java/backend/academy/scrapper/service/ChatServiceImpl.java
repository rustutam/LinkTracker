package backend.academy.scrapper.service;

import backend.academy.scrapper.repository.LinksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private LinksRepository linksRepository;

    @Override
    public void register(long chatId) {
        linksRepository.register(chatId);
    }

    @Override
    public void unRegister(long chatId) {
        linksRepository.unRegister(chatId);
    }
}
