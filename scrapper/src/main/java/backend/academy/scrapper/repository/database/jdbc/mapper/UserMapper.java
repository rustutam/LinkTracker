package backend.academy.scrapper.repository.database.jdbc.mapper;

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
}
