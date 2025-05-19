package backend.academy.scrapper.client;

import backend.academy.scrapper.exceptions.ApiBotErrorResponseException;
import backend.academy.scrapper.scheduler.CheckUpdateScheduler;
import backend.academy.scrapper.scheduler.SendUpdateScheduler;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dto.LinkUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WireMockTest(httpPort = 8080)
class BotRetryProxyTest {
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

