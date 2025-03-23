package backend.academy.scrapper.repository.database.utilities.mapper;

import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.UserId;
import backend.academy.scrapper.models.entities.UserEntity;

public class UserMapper {
    public static User toDomain(UserEntity entity) {
        return new User(
            new UserId(entity.id()),
            new ChatId(entity.chatId())
        );
    }

    public static UserEntity toEntity(User domain) {
        return new UserEntity(
            domain.userId().id(),
            domain.chatId().id(),
            null
        );
    }
}
