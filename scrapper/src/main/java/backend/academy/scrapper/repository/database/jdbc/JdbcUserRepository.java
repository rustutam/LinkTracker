package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.exceptions.DoubleRegistrationException;
import backend.academy.scrapper.exceptions.NotExistUserException;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.UserId;
import backend.academy.scrapper.repository.database.UserRepository;
import backend.academy.scrapper.repository.database.jdbc.mapper.UserRowMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcUserRepository implements UserRepository {
    private static final String SELECT_BY_ID = "SELECT * FROM scrapper.users WHERE id = ?";
    private static final String SELECT_BY_CHAT_ID = "SELECT * FROM scrapper.users WHERE chat_id = ?";
    private static final String INSERT_SQL = "INSERT INTO scrapper.users (chat_id) VALUES (?)";
    private static final String DELETE_BY_CHAT_ID = "DELETE FROM scrapper.users WHERE chat_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper rowMapper;

    @Override
    public Optional<User> findById(UserId userId) {
        List<User> users = jdbcTemplate.query(SELECT_BY_ID, rowMapper, userId.id());
        return Optional.ofNullable(DataAccessUtils.singleResult(users));
    }

    @Override
    public Optional<User> findByChatId(ChatId chatId) {
        List<User> users = jdbcTemplate.query(SELECT_BY_CHAT_ID, rowMapper, chatId.id());
        return Optional.ofNullable(DataAccessUtils.singleResult(users));
    }

    @Override
    @Transactional
    public void save(ChatId chatId) throws DoubleRegistrationException {
        try {
            jdbcTemplate.update(INSERT_SQL, chatId.id());
        } catch (DataIntegrityViolationException ex) {
            log.atError()
                    .addKeyValue("chatId", chatId.id())
                    .addKeyValue("access-type", "SQL")
                    .setMessage("Ошибка сохранения: пользователь уже существует")
                    .log();
            throw new DoubleRegistrationException();
        }
    }

    @Override
    @Transactional
    public void deleteByChatId(ChatId chatId) throws NotExistUserException {
        int rows = jdbcTemplate.update(DELETE_BY_CHAT_ID, chatId.id());
        if (rows == 0) {
            throw new NotExistUserException();
        }
    }
}
