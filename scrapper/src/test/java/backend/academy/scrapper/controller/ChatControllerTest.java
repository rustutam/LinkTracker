package backend.academy.scrapper.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.repository.database.jdbc.JdbcUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ChatControllerTest extends IntegrationEnvironment {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcUserRepository userRepository;

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void tgChatIdPost_ValidId_ReturnsOk() throws Exception {
        ChatId chatId = new ChatId(123L);

        mockMvc.perform(post("/tg-chat/{id}", chatId.id())).andExpect(status().isOk());

        assertTrue(userRepository.findByChatId(chatId).isPresent());
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void doubleRegistrationTest() throws Exception {
        ChatId chatId = new ChatId(123L);

        // Первая регистрация
        mockMvc.perform(post("/tg-chat/{id}", chatId.id())).andExpect(status().isOk());

        // Вторая регистрация, должна вернуться 406 ответ
        mockMvc.perform(post("/tg-chat/{id}", chatId.id())).andExpect(status().isNotAcceptable());
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void tgChatIdDelete_ValidId_ReturnsOk() throws Exception {
        ChatId chatId = new ChatId(123L);

        // Регистрация
        mockMvc.perform(post("/tg-chat/{id}", chatId.id())).andExpect(status().isOk());

        // Удаление
        mockMvc.perform(delete("/tg-chat/{id}", chatId.id())).andExpect(status().isOk());

        assertTrue(userRepository.findByChatId(chatId).isEmpty());
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void whenChatDeleteWithUnauthorizedUserThenReturnUnauthorized() throws Exception {
        ChatId chatId = new ChatId(123L);

        // Удаление не зарегистрированного чата, должно вернуть 401
        mockMvc.perform(delete("/tg-chat/{id}", chatId.id())).andExpect(status().isUnauthorized());

        assertTrue(userRepository.findByChatId(chatId).isEmpty());
    }
}
