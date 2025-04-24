package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.exceptions.DoubleRegistrationException;
import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.UserId;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JdbcUserRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcUserRepository jdbcChatRepository;

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdTest() {
        UserId userId = new UserId(1L);
        ChatId expectedChatId = new ChatId(100L);

        Optional<User> user = jdbcChatRepository.findById(userId);

        assertTrue(user.isPresent());
        assertEquals(userId, user.get().userId());
        assertEquals(expectedChatId, user.get().chatId());
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdWhenLinkNotFoundTest() {
        UserId userId = new UserId(101L);

        Optional<User> user = jdbcChatRepository.findById(userId);

        assertTrue(user.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByChatIdTest() {
        UserId expectedUserId = new UserId(1L);
        ChatId chatId = new ChatId(100L);

        Optional<User> user = jdbcChatRepository.findByChatId(chatId);

        assertTrue(user.isPresent());
        assertEquals(chatId, user.get().chatId());
        assertEquals(expectedUserId, user.get().userId());
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByChatIdWhenLinkNotFoundTest() {
        ChatId chatId = new ChatId(10L);

        Optional<User> user = jdbcChatRepository.findByChatId(chatId);

        assertTrue(user.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveUserSuccessfullyTest() {
        ChatId chatId = new ChatId(10L);

        jdbcChatRepository.save(chatId);

        assertTrue(jdbcChatRepository.findByChatId(chatId).isPresent());
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void doubleRegistrationThrowsExceptionTest() {
        ChatId chatId = new ChatId(100L);

        assertThrows(DoubleRegistrationException.class, () -> jdbcChatRepository.save(chatId));
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void doubleRegistrationThrowsExceptionWhenDoubleRegistrationTest() {
        ChatId chatId = new ChatId(99L);

        jdbcChatRepository.save(chatId);

        assertTrue(jdbcChatRepository.findByChatId(chatId).isPresent());
        assertThrows(DoubleRegistrationException.class, () -> jdbcChatRepository.save(chatId));
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByChatIdSuccessfullyTest() {
        ChatId chatId = new ChatId(100L);

        jdbcChatRepository.deleteByChatId(chatId);

        assertTrue(jdbcChatRepository.findByChatId(chatId).isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByChatIdSuccessfullyWhenDeleteMultipleUsersTest() {
        ChatId firstChatId = new ChatId(100L);
        ChatId secondChatId = new ChatId(101L);
        ChatId thirdChatId = new ChatId(102L);
        ChatId fourthChatId = new ChatId(103L);
        ChatId fifthChatId = new ChatId(104L);

        jdbcChatRepository.deleteByChatId(firstChatId);
        jdbcChatRepository.deleteByChatId(secondChatId);
        jdbcChatRepository.deleteByChatId(thirdChatId);
        jdbcChatRepository.deleteByChatId(fourthChatId);

        assertTrue(jdbcChatRepository.findByChatId(firstChatId).isEmpty());
        assertTrue(jdbcChatRepository.findByChatId(secondChatId).isEmpty());
        assertTrue(jdbcChatRepository.findByChatId(thirdChatId).isEmpty());
        assertTrue(jdbcChatRepository.findByChatId(fourthChatId).isEmpty());
        assertTrue(jdbcChatRepository.findByChatId(fourthChatId).isEmpty());
        assertTrue(jdbcChatRepository.findByChatId(fifthChatId).isPresent());
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void notExistTgChatThrowsExceptionTest() {
        ChatId chatId = new ChatId(99L);

        assertThrows(NotExistTgChatException.class, () -> jdbcChatRepository.deleteByChatId(chatId));
    }


}
