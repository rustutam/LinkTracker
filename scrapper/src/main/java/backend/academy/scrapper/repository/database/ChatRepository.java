package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.exceptions.DoubleRegistrationException;
import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.UserId;
import java.util.Optional;

public interface ChatRepository {
    Optional<User> findById(UserId userId);

    Optional<User> findByChatId(ChatId chatId);

    void save(ChatId chatId) throws DoubleRegistrationException;

    void deleteByChatId(ChatId chatId) throws NotExistTgChatException;
}
