package backend.academy.scrapper;

import java.util.ArrayList;
import backend.academy.scrapper.configuration.KafkaConfig;
import backend.academy.scrapper.configuration.RetryCodesConfig;
import backend.academy.scrapper.configuration.SchedulerConfig;
import backend.academy.scrapper.configuration.ScrapperConfig;
import backend.academy.scrapper.configuration.clients.BotConfig;
import backend.academy.scrapper.configuration.clients.GitHubConfig;
import backend.academy.scrapper.configuration.clients.StackOverflowConfig;
import backend.academy.scrapper.sender.BotHttpSender;
import backend.academy.scrapper.sender.BotKafkaSender;
import backend.academy.scrapper.sender.ResistantProxy;
import dto.LinkUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest(classes = TestApplication.class)
@ExtendWith(SpringExtension.class)
@Import({ResistantProxy.class, TestcontainersConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAutoConfiguration
@EnableConfigurationProperties({
    ScrapperConfig.class,
    GitHubConfig.class,
    StackOverflowConfig.class,
    BotConfig.class,
    SchedulerConfig.class,
    KafkaConfig.class,
    RetryCodesConfig.class,
})
@Testcontainers
public class FallbackTest {
    @Autowired
    private ResistantProxy proxy;

    @MockitoBean
    public BotHttpSender httpMock = mock(BotHttpSender.class);

    @MockitoBean
    public BotKafkaSender kafkaMock = mock(BotKafkaSender.class);


    @Test
    void fallback() {
        LinkUpdate linkUpdate = new LinkUpdate(1L, "", "req", new ArrayList<>());

        assertTrue("msg", AopUtils.isAopProxy(proxy));
        doThrow(new RuntimeException()).when(httpMock).sendUpdates(any());

        proxy.sendUpdates(linkUpdate);

        verify(kafkaMock, timeout(10000)).sendUpdates(linkUpdate);
    }
}
