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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcTemplate jdbcTemplate;

    /**
     * @param userId
     * @return User
     */
    @Override
    public Optional<User> findById(UserId userId){
        var resultChats = jdbcTemplate.query(
            "SELECT * FROM users WHERE id = (?)",
            JdbcRowMapperUtil::mapRowToUserEntity,
            userId
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
            "SELECT * FROM users WHERE chat_id = (?)",
            JdbcRowMapperUtil::mapRowToUserEntity,
            chatId
        );

        return userEntities.stream().map(UserMapper::toDomain).findFirst();
    }

    /**
     * @param chatId
     */
    @Override
    public void save(ChatId chatId) throws DoubleRegistrationException {
        //TODO проверить выбрасывается ли ошибка
        try {
            jdbcTemplate.update(
                "INSERT INTO users (chat_id) VALUES (?)",
                chatId.id()
            );
        } catch (DataIntegrityViolationException e) {
            throw new DoubleRegistrationException();
        }
    }

    /**
     * @param chatId
     */
    @Override
    public void deleteByChatId(ChatId chatId) throws NotExistTgChatException {
        int updatedRows = jdbcTemplate.update(
            "DELETE FROM users WHERE chat_id = (?)",
            chatId.id()
        );

        if (updatedRows == 0) {
            throw new NotExistTgChatException();
        }
    }
}
