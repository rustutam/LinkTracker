package backend.academy.scrapper.repository.database.jpa.mapper;

import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.UserId;
import backend.academy.scrapper.models.entity.UserEntity;

public class UserMapper {
    public static User map(UserEntity userEntity) {
        return new User(
            new UserId(userEntity.id()),
            new ChatId(userEntity.chatId())
        );
    }

}
