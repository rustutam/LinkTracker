package backend.academy.scrapper.service;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.exceptions.DoubleRegistrationException;
import backend.academy.scrapper.exceptions.NotExistUserException;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.repository.database.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ChatServiceImplTest extends IntegrationEnvironment {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatService chatService;

    @Test
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Выбросить DoubleRegistrationException, если чат уже зарегистрирован")
    void register_ShouldThrowDoubleRegistrationException_WhenChatAlreadyExists() {
        ChatId chatId = new ChatId(123L);

        userRepository.save(chatId);
        assertThrows(DoubleRegistrationException.class, () -> chatService.register(chatId));
    }

    @Test
    @DisplayName("Зарегистрировать чат, если его нет в базе")
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void register_ShouldRegisterChat_WhenChatDoesNotExist() {
        ChatId chatId = new ChatId(123L);

        chatService.register(chatId);

        assertTrue(userRepository.findByChatId(chatId).isPresent());
    }

    @Test
    @DisplayName("Выбросить NotExistTgChatException, если чата нет в базе")
    void unRegister_ShouldThrowNotExistTgChatException_WhenChatDoesNotExist() {
        ChatId chatId = new ChatId(123L);

        assertThrows(NotExistUserException.class, () -> chatService.unRegister(chatId));
    }

    @Test
    @DisplayName("Удалить чат, если он есть в базе")
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unRegister_ShouldUnregisterChat_WhenChatExists() {
        ChatId chatId = new ChatId(101L);

        chatService.unRegister(chatId);

        assertTrue(userRepository.findByChatId(chatId).isEmpty());
    }
}
