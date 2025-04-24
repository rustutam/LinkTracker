package backend.academy.scrapper.repository.database.utilities.mapper;

import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.UserId;
import backend.academy.scrapper.models.dto.UserDto;

public class UserMapper {
    public static User toDomain(UserDto entity) {
        return new User(
            new UserId(entity.id()),
            new ChatId(entity.chatId())
        );
    }

    public static UserDto toEntity(User domain) {
        return new UserDto(
            domain.userId().id(),
            domain.chatId().id(),
            null
        );
    }
}
