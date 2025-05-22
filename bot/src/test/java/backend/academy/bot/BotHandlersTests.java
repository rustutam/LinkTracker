package backend.academy.bot;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.bot.api.services.LinkTrackerHandlers;
import backend.academy.bot.api.services.commands.Command;
import backend.academy.bot.api.services.scrapper.ApiScrapper;
import backend.academy.bot.sender.BotSender;
import backend.academy.bot.api.tg.FSM;
import backend.academy.bot.api.tg.State;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class BotHandlersTests {
    @MockitoBean
    private BotSender mockBotSender;

    @MockitoBean
    private ApiScrapper mockApiScrapper;

    private Message mockMessage;
    private Chat mockChat;
    private LinkTrackerHandlers handler;
    private FSM fsm;

    @Autowired
    private List<Command> handlers;

    @BeforeEach
    public void setup() {
        mockMessage = mock(Message.class);
        mockChat = mock(Chat.class);
        fsm = new FSM();
        handler = new LinkTrackerHandlers(handlers);
    }

    @AfterEach
    public void cleanup() {
        fsm.setCurrentState(State.None);
        reset(mockBotSender);
        reset(mockApiScrapper);
        reset(mockMessage);
        reset(mockChat);
    }

    @Test
    public void incorrectCommand() {
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockChat.id()).thenReturn(1L);
        when(mockBotSender.sendMessage(mockMessage.chat().id(), "_I do not know this command!_", ParseMode.Markdown))
                .thenReturn(true);

        handler.handle(mockMessage, "unknown_command", fsm, null);

        verify(mockBotSender)
                .sendMessage(mockMessage.chat().id(), "_I do not know this command!_", ParseMode.Markdown);
    }
}
