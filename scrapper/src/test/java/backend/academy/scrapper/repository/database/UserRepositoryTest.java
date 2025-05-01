package backend.academy.scrapper.repository.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.exceptions.DoubleRegistrationException;
import backend.academy.scrapper.exceptions.NotExistUserException;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.UserId;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

public abstract class UserRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdTest() {
        UserId userId = new UserId(1L);
        ChatId expectedChatId = new ChatId(100L);
        OffsetDateTime expectedCreatedAt = OffsetDateTime.parse("2024-10-01T21:35:03Z");

        Optional<User> user = userRepository.findById(userId);

        assertTrue(user.isPresent());
        assertEquals(userId, user.get().userId());
        assertEquals(expectedChatId, user.get().chatId());
        assertEquals(expectedCreatedAt, user.get().createdAt());
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdWhenLinkNotFoundTest() {
        UserId userId = new UserId(99L);

        Optional<User> user = userRepository.findById(userId);

        assertTrue(user.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByChatIdTest() {
        ChatId chatId = new ChatId(100L);
        UserId expectedUserId = new UserId(1L);
        OffsetDateTime expectedCreatedAt = OffsetDateTime.parse("2024-10-01T21:35:03Z");

        Optional<User> user = userRepository.findByChatId(chatId);

        assertTrue(user.isPresent());
        assertEquals(chatId, user.get().chatId());
        assertEquals(expectedUserId, user.get().userId());
        assertEquals(expectedCreatedAt, user.get().createdAt());
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByChatIdWhenLinkNotFoundTest() {
        ChatId chatId = new ChatId(10L);

        Optional<User> user = userRepository.findByChatId(chatId);

        assertTrue(user.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveUserSuccessfullyTest() {
        ChatId chatId = new ChatId(10L);

        userRepository.save(chatId);

        assertTrue(userRepository.findByChatId(chatId).isPresent());
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void doubleRegistrationThrowsExceptionTest() {
        ChatId chatId = new ChatId(100L);

        assertThrows(DoubleRegistrationException.class, () -> userRepository.save(chatId));
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void doubleRegistrationThrowsExceptionWhenDoubleRegistrationTest() {
        ChatId chatId = new ChatId(99L);

        userRepository.save(chatId);

        assertTrue(userRepository.findByChatId(chatId).isPresent());
        assertThrows(DoubleRegistrationException.class, () -> userRepository.save(chatId));
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByChatIdSuccessfullyTest() {
        ChatId chatId = new ChatId(100L);

        userRepository.deleteByChatId(chatId);

        assertTrue(userRepository.findByChatId(chatId).isEmpty());
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

        userRepository.deleteByChatId(firstChatId);
        userRepository.deleteByChatId(secondChatId);
        userRepository.deleteByChatId(thirdChatId);
        userRepository.deleteByChatId(fourthChatId);

        assertTrue(userRepository.findByChatId(firstChatId).isEmpty());
        assertTrue(userRepository.findByChatId(secondChatId).isEmpty());
        assertTrue(userRepository.findByChatId(thirdChatId).isEmpty());
        assertTrue(userRepository.findByChatId(fourthChatId).isEmpty());
        assertTrue(userRepository.findByChatId(fourthChatId).isEmpty());
        assertTrue(userRepository.findByChatId(fifthChatId).isPresent());
    }

    @Test
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void notExistTgChatThrowsExceptionTest() {
        ChatId chatId = new ChatId(99L);

        assertThrows(NotExistUserException.class, () -> userRepository.deleteByChatId(chatId));
    }
}
