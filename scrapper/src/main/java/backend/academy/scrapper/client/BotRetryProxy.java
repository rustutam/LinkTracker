package backend.academy.scrapper.client;

import backend.academy.scrapper.configuration.clients.BotConfig;
import backend.academy.scrapper.exceptions.ApiBotErrorResponseException;
import dto.LinkUpdate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class BotRetryProxy {
    private final RestClient restClient;

    public BotRetryProxy(BotConfig botConfig) {
        restClient = botConfig.botRestClient();
    }

    @Retry(name = "bot")
    @CircuitBreaker(name = "cb1")
    public void sendUpdates(LinkUpdate linkUpdate) throws ApiBotErrorResponseException {
        try {
            restClient.post().uri("/updates").body(linkUpdate).retrieve().toBodilessEntity();

            log.atInfo()
                    .addKeyValue("link", linkUpdate.url())
                    .setMessage("Отправка обновления по HTTP")
                    .log();
        } catch (HttpClientErrorException e) {
            log.atError()
                    .setMessage("Ошибка отправки обновлений боту")
                    .addKeyValue("link", linkUpdate.url())
                    .addKeyValue("statusCode", e.getStatusCode().value())
                    .addKeyValue("statusText", e.getStatusText())
                    .log();

            throw new ApiBotErrorResponseException(
                    "Ошибка отправки обновлений боту",
                    e.getStatusCode(),
                    e.getStatusText(),
                    e.getMessage(),
                    e.getStackTrace());
        }
    }
}
