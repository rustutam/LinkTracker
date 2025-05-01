package backend.academy.scrapper.repository.database.jpa.mapper;

import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.UserId;
import backend.academy.scrapper.models.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toDomain(UserEntity entity) {
        return new User(new UserId(entity.id()), new ChatId(entity.chatId()), entity.createdAt());
    }

    public UserEntity toEntity(User domain) {
        UserEntity entity = new UserEntity();
        if (domain.userId() != null && domain.userId().id() != 0) {
            entity.id(domain.userId().id());
        }
        entity.chatId(domain.chatId().id());
        entity.createdAt(domain.createdAt());
        return entity;
    }
}
