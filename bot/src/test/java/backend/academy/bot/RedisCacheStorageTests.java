package backend.academy.bot;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import backend.academy.bot.api.cache.KeyGenerator;
import backend.academy.bot.api.cache.RedisCacheStorage;
import backend.academy.bot.api.dto.ListLinksItem;
import backend.academy.bot.api.dto.ListLinksResponse;
import backend.academy.bot.api.services.commands.EnterFiltersCommand;
import backend.academy.bot.api.services.commands.ListCommand;
import backend.academy.bot.api.services.commands.UntrackCommand;
import backend.academy.bot.api.services.scrapper.ApiScrapper;
import backend.academy.bot.sender.BotSender;
import backend.academy.bot.api.tg.FSM;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class RedisCacheStorageTests {
    private final GenericContainer<?> redisContainer;
    private final RedisCacheStorage cache;

    @MockitoBean
    private BotSender mockBotSender;

    @MockitoBean
    private ApiScrapper mockApiScrapper;

    private Message mockMessage;
    private Chat mockChat;
    private FSM fsm;

    @Autowired
    public RedisCacheStorageTests(GenericContainer<?> redisContainer, RedisCacheStorage cache) {
        this.redisContainer = redisContainer;
        this.cache = cache;
    }

    @BeforeEach
    public void setup() {
        mockMessage = mock(Message.class);
        mockChat = mock(Chat.class);
        fsm = new FSM();
    }

    @PostConstruct
    public void setUp() {
        redisContainer.start();
    }

    @Test
    public void newCacheWhenListCommand() {
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockChat.id()).thenReturn(1L);
        when(mockBotSender.sendMessage(any(), any(), any())).thenReturn(true);
        var arr = new ArrayList<ListLinksItem>();
        arr.add(new ListLinksItem(1L, "github.com", new ArrayList<>(), new ArrayList<>()));
        when(mockApiScrapper.getLinks(any())).thenReturn(new ListLinksResponse(arr.size(), arr));
        KeyGenerator generator = new KeyGenerator();
        ListCommand cmd = new ListCommand(mockApiScrapper, mockBotSender, cache, generator);
        Map<Long, Map<String, String>> userData = new HashMap<>();

        cmd.execute(mockMessage, fsm, userData);

        Assertions.assertThat(cache.hasKey(generator.listCommand(mockMessage))).isTrue();
    }

    @Test
    public void invalidateCacheWhenEnterFiltersCommand() {
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockMessage.text()).thenReturn("f1 f2");
        when(mockChat.id()).thenReturn(1L);
        when(mockBotSender.sendMessage(any(), any(), any())).thenReturn(true);
        KeyGenerator generator = new KeyGenerator();
        EnterFiltersCommand cmd = new EnterFiltersCommand(mockApiScrapper, mockBotSender, cache, generator);
        Map<Long, Map<String, String>> userData = new HashMap<>();
        userData.put(1L, new HashMap<>());
        userData.get(1L).put("url", "github.com");
        userData.get(1L).put("tags", "t1");

        cmd.execute(mockMessage, fsm, userData);

        Assertions.assertThat(cache.hasKey(generator.listCommand(mockMessage))).isFalse();
    }

    @Test
    public void invalidateCacheWhenUntrackCommand() {
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockMessage.text()).thenReturn("TEXT TEXT");
        when(mockChat.id()).thenReturn(1L);
        when(mockBotSender.sendMessage(any(), any(), any())).thenReturn(true);
        KeyGenerator generator = new KeyGenerator();
        UntrackCommand cmd = new UntrackCommand(mockApiScrapper, mockBotSender, cache, generator);
        Map<Long, Map<String, String>> userData = new HashMap<>();

        cmd.execute(mockMessage, fsm, userData);

        Assertions.assertThat(cache.hasKey(generator.listCommand(mockMessage))).isFalse();
    }
}
