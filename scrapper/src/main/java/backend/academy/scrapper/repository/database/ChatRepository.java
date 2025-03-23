package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.UserId;

public interface ChatRepository {
    User findById(UserId userId);

    User findByChatId(ChatId chatId) throws NotExistTgChatException;

    void save(ChatId chatId);

    void deleteByChatId(ChatId chatId);
}
