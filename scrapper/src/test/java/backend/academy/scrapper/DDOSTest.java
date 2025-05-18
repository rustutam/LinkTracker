package backend.academy.scrapper;

import backend.academy.scrapper.controller.ChatController;
import backend.academy.scrapper.service.ChatService;
import io.github.resilience4j.springboot3.ratelimiter.autoconfigure.RateLimiterAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({RateLimiterAutoConfiguration.class})
class DDOSTest {
    @MockitoBean
    private ChatService mockChat;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void ddos() throws Exception {
        mockMvc.perform(post("/tg-chat/1")).andExpect(status().isOk());
        mockMvc.perform(post("/tg-chat/1")).andExpect(status().isOk());
        mockMvc.perform(post("/tg-chat/1")).andExpect(status().isTooManyRequests());
    }
}
