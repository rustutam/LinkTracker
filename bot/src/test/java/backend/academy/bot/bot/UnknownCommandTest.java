package backend.academy.bot.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.bot.service.BotService;
import backend.academy.bot.utils.BotMessages;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class UnknownCommandTest extends BaseConfigure {
    @MockitoBean
    KafkaAdmin kafkaAdmin;

    @MockitoBean
    private TelegramBot telegramBot;

    @Autowired
    private BotService botService;

    @Test
    @DisplayName("Тестирование вызова неизвестной команды")
    public void test() {
        when(message.text()).thenReturn("/wrongCommand");

        botService.handleMessage(update);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());

        SendMessage sentMessage = captor.getValue();
        assertEquals(123L, sentMessage.getParameters().get("chat_id"));
        assertEquals(BotMessages.WRONG_COMMAND, sentMessage.getParameters().get("text"));
    }
}
