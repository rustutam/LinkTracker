package backend.academy.scrapper.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.*;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.exceptions.ApiBotErrorResponseException;
import backend.academy.scrapper.scheduler.CheckUpdateScheduler;
import backend.academy.scrapper.scheduler.SendUpdateScheduler;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dto.LinkUpdate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ActiveProfiles("test")
@WireMockTest(httpPort = 9999)
class BotRetryProxyTest extends IntegrationEnvironment {
    @MockitoBean
    CheckUpdateScheduler checkUpdateScheduler;

    @MockitoBean
    SendUpdateScheduler sendUpdateScheduler;

    @Autowired
    private BotRetryProxy botRetryProxy;

    @BeforeEach
    void setupWireMock() {
        WireMock.reset();

        stubFor(post(urlEqualTo("/updates"))
                .inScenario("Retry Scenario")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse().withStatus(400))
                .willSetStateTo("Second Attempt"));
    }

    @Test
    void testRetryThreeAttempts() {
        LinkUpdate update = new LinkUpdate(1L, "https://test", "desc", List.of(123L));

        assertThrows(ApiBotErrorResponseException.class, () -> botRetryProxy.sendUpdates(update));
    }
}
