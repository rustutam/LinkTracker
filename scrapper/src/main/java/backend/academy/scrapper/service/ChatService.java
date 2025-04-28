package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.ids.ChatId;

public interface ChatService {
    void register(ChatId chatId);

    void unRegister(ChatId chatId);
}
