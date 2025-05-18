package backend.academy.scrapper;

import backend.academy.scrapper.client.BotRetryProxy;
import backend.academy.scrapper.configuration.KafkaConfig;
import backend.academy.scrapper.configuration.RetryCodesConfig;
import backend.academy.scrapper.configuration.SchedulerConfig;
import backend.academy.scrapper.configuration.ScrapperConfig;
import backend.academy.scrapper.configuration.clients.BotConfig;
import backend.academy.scrapper.configuration.clients.GitHubConfig;
import backend.academy.scrapper.configuration.clients.StackOverflowConfig;
import backend.academy.scrapper.sender.BotHttpSender;
import dto.LinkUpdate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.invocation.Invocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@TestConfiguration
class TestConfig {
    public RestTemplateBuilder mockRest = mock(RestTemplateBuilder.class);
    public RestTemplate template = mock(RestTemplate.class, invocation -> {
        throw new RuntimeException();
    });

    @Bean
    public RestTemplateBuilder builder() {
        when(mockRest.build()).thenReturn(template);
        return mockRest;
    }
}

@SpringBootTest(classes = TestApplication.class)
@ExtendWith(SpringExtension.class)
@Import({BotHttpSender.class, TestcontainersConfiguration.class, TestConfig.class})
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
public class RetryTest {
    @Autowired
    private BotRetryProxy proxy;

    @Autowired
    private TestConfig config;

    @Test
    void testRetries() {
        LinkUpdate linkUpdate = new LinkUpdate(1L, "", "req", new ArrayList<>());

        assertTrue("", AopUtils.isAopProxy(proxy));

        assertThrows(RuntimeException.class, () -> {
            proxy.sendUpdates(linkUpdate);
        });

        Collection<Invocation> invocations = mockingDetails(config.template).getInvocations();
        assertThat(invocations.size()).isEqualTo(3);
    }
}
