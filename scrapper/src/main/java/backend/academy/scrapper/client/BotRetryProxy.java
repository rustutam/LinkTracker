package backend.academy.scrapper.client;

import backend.academy.scrapper.configuration.clients.BotConfig;
import backend.academy.scrapper.exceptions.ApiErrorResponseException;
import dto.LinkUpdate;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "app", name = "message-transport", havingValue = "Http")
public class BotRetryProxy {
    private final RestClient restClient;

    public BotRetryProxy(BotConfig botConfig) {
        restClient = botConfig.botRestClient();
    }

    @SuppressFBWarnings(
        value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
        justification = "Когда я ловлю ошибку, она не null")
    @Retry(name = "bot")
    @CircuitBreaker(name = "cb1")
    public void sendUpdates(LinkUpdate linkUpdate) throws ApiErrorResponseException {
        try {
            restClient.post().uri("/updates").body(linkUpdate).retrieve().toBodilessEntity();

            log.atInfo()
                .addKeyValue("link", linkUpdate.url())
                .setMessage("Отправка обновления по HTTP")
                .log();
        } catch (HttpClientErrorException e) {
            throw e.getResponseBodyAs(ApiErrorResponseException.class);
        }
    }
}
