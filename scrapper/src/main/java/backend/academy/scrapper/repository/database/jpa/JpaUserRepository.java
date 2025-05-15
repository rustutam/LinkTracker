package backend.academy.scrapper.repository.database.jpa;

import backend.academy.scrapper.exceptions.DoubleRegistrationException;
import backend.academy.scrapper.exceptions.NotExistUserException;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.UserId;
import backend.academy.scrapper.models.entity.UserEntity;
import backend.academy.scrapper.repository.database.UserRepository;
import backend.academy.scrapper.repository.database.jpa.mapper.UserMapper;
import backend.academy.scrapper.repository.database.jpa.repo.UserRepo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "ORM")
public class JpaUserRepository implements UserRepository {
    private final UserRepo userRepo;
    private final UserMapper mapper;

    @Override
    public Optional<User> findById(UserId userId) {
        return userRepo.findById(userId.id()).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByChatId(ChatId chatId) {
        return userRepo.findByChatId(chatId.id()).map(mapper::toDomain);
    }

    @Override
    public void save(ChatId chatId) throws DoubleRegistrationException {
        if (userRepo.existsByChatId(chatId.id())) {
            log.atError()
                    .addKeyValue("chatId", chatId)
                    .addKeyValue("access-type", "ORM")
                    .setMessage("Ошибка сохранения: пользователь уже существует")
                    .log();

            throw new DoubleRegistrationException();
        }

        UserEntity userEntity = new UserEntity();
        userEntity.chatId(chatId.id());

        userRepo.save(userEntity);
    }

    @Override
    public void deleteByChatId(ChatId chatId) throws NotExistUserException {
        UserEntity user = userRepo.findByChatId(chatId.id()).orElseThrow(NotExistUserException::new);

        userRepo.delete(user);
    }
}
