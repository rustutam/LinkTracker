package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.exceptions.DoubleRegistrationException;
import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.UserId;
import backend.academy.scrapper.repository.database.ChatRepository;
import backend.academy.scrapper.repository.database.utilities.JdbcRowMapperUtil;
import backend.academy.scrapper.repository.database.utilities.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcChatRepository implements ChatRepository {
    private final JdbcTemplate jdbcTemplate;

    /**
     * @param userId
     * @return User
     */
    @Override
    public Optional<User> findById(UserId userId){
        var resultChats = jdbcTemplate.query(
            "SELECT * FROM scrapper.users WHERE id = (?)",
            JdbcRowMapperUtil::mapRowToUserEntity,
            userId.id()
        );
        return resultChats.stream().map(UserMapper::toDomain).findFirst();
    }

    /**
     * @param chatId
     * @return User
     */
    @Override
    public Optional<User> findByChatId(ChatId chatId){
        var userEntities = jdbcTemplate.query(
            "SELECT * FROM scrapper.users WHERE chat_id = (?)",
            JdbcRowMapperUtil::mapRowToUserEntity,
            chatId.id()
        );

        return userEntities.stream().map(UserMapper::toDomain).findFirst();
    }

    /**
     * @param chatId
     */
    @Override
    public void save(ChatId chatId) throws DoubleRegistrationException {
        try {
            jdbcTemplate.update(
                "INSERT INTO scrapper.users (chat_id) VALUES (?)",
                chatId.id()
            );
        } catch (DataIntegrityViolationException e) {
            log.atError()
                .addKeyValue("chatId", chatId)
                .setMessage("Ошибка сохранения в базу данных пользователя")
                .setMessage("Такой пользователь уже существует")
                .log();
            throw new DoubleRegistrationException();
        }
    }

    /**
     * @param chatId
     */
    @Override
    public void deleteByChatId(ChatId chatId) throws NotExistTgChatException {
        int updatedRows = jdbcTemplate.update(
            "DELETE FROM scrapper.users WHERE chat_id = (?)",
            chatId.id()
        );

        if (updatedRows == 0) {
            throw new NotExistTgChatException();
        }
    }
}
