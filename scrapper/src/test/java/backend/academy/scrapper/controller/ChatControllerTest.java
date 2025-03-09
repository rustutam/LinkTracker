package backend.academy.scrapper.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.academy.scrapper.handler.ChatHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ChatController.class)
@AutoConfigureMockMvc
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatHandler chatHandler;

    @Test
    void tgChatIdPost_ValidId_ReturnsOk() throws Exception {
        Long chatId = 1L;

        mockMvc.perform(post("/tg-chat/{id}", chatId)).andExpect(status().isOk());

        verify(chatHandler, times(1)).register(chatId);
    }

    @Test
    void tgChatIdPost_InvalidId_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/tg-chat/{id}", "invalid")).andExpect(status().isBadRequest());

        verifyNoInteractions(chatHandler);
    }

    @Test
    void tgChatIdDelete_ValidId_ReturnsOk() throws Exception {
        Long chatId = 1L;

        mockMvc.perform(delete("/tg-chat/{id}", chatId)).andExpect(status().isOk());

        verify(chatHandler, times(1)).unregister(chatId);
    }

    @Test
    void tgChatIdDelete_InvalidId_ReturnsBadRequest() throws Exception {
        mockMvc.perform(delete("/tg-chat/{id}", "invalid")).andExpect(status().isBadRequest());

        verifyNoInteractions(chatHandler);
    }
}
