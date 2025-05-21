package backend.academy.bot.api.services.scrapper;

import backend.academy.bot.api.dto.AddLinkRequest;
import backend.academy.bot.api.dto.ApiErrorResponse;
import backend.academy.bot.api.dto.LinkResponse;
import backend.academy.bot.api.dto.ListLinksResponse;
import backend.academy.bot.api.dto.RemoveLinkRequest;
import backend.academy.bot.config.clients.ScrapperConfig;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import static backend.academy.bot.utils.LogMessages.CHAT_ID;
import static backend.academy.bot.utils.LogMessages.URL;

@Slf4j
@Service
public class ScrapperRetryProxy {
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private static final String TG_CHAT_PATH = "/tg-chat";
    private static final String LINKS_PATH = "/links";
    private final RestClient restClient;

    @Autowired
    public ScrapperRetryProxy(ScrapperConfig scrapperConfig) {
        this.restClient = scrapperConfig.restClient();
    }

    @Retry(name = "retryRules", fallbackMethod = "handleException")
    @CircuitBreaker(name = "cb1")
    public void registerChat(Long chatId) throws ApiErrorResponse {
        try {
            restClient.post().uri("/tg-chat/{id}", chatId).retrieve().toBodilessEntity();
        } catch (HttpClientErrorException e) {
            log.atError()
                .setMessage("Ошибка отправки запроса на регистрацию чата")
                .addKeyValue(CHAT_ID, chatId)
                .addKeyValue("statusCode", e.getStatusCode().value())
                .addKeyValue("statusText", e.getStatusText())
                .log();

            throw new ApiErrorResponse(
                "Ошибка отправки запроса на регистрацию чата",
                e.getStatusCode(),
                e.getStatusText(),
                e.getMessage(),
                e.getStackTrace());
        }
    }

    @Retry(name = "retryRules", fallbackMethod = "handleException")
    @CircuitBreaker(name = "cb1")
    public void deleteChat(Long chatId) throws ApiErrorResponse {
        try {
            restClient.delete().uri("/tg-chat/{id}", chatId).retrieve().toBodilessEntity();
        } catch (HttpClientErrorException e) {
            log.atError()
                .setMessage("Ошибка отправки запроса на удаление чата")
                .addKeyValue(CHAT_ID, chatId)
                .addKeyValue("statusCode", e.getStatusCode().value())
                .addKeyValue("statusText", e.getStatusText())
                .log();

            throw new ApiErrorResponse(
                "Ошибка отправки запроса на удаление чата",
                e.getStatusCode(),
                e.getStatusText(),
                e.getMessage(),
                e.getStackTrace());
        }
    }

    @Retry(name = "retryRules", fallbackMethod = "handleException")
    @CircuitBreaker(name = "cb1")
    public ListLinksResponse getLinks(Long chatId) throws ApiErrorResponse {
        try {
            return restClient
                .get()
                .uri("/links")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .retrieve()
                .body(ListLinksResponse.class);
        } catch (HttpClientErrorException e) {
            log.atError()
                .setMessage("Ошибка отправки запроса на получение всех ссылок пользователя")
                .addKeyValue(CHAT_ID, chatId)
                .addKeyValue("statusCode", e.getStatusCode().value())
                .addKeyValue("statusText", e.getStatusText())
                .log();

            throw new ApiErrorResponse(
                "Ошибка отправки запроса на получение всех ссылок пользователя",
                e.getStatusCode(),
                e.getStatusText(),
                e.getMessage(),
                e.getStackTrace());
        }
    }

    @Retry(name = "retryRules", fallbackMethod = "handleException")
    @CircuitBreaker(name = "cb1")
    public LinkResponse subscribeToLink(Long chatId, String link, List<String> tags, List<String> filters)
        throws ApiErrorResponse {
        AddLinkRequest requestToScrapper = new AddLinkRequest(link, tags, filters);
        try {
            return restClient
                .post()
                .uri("/links")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .body(requestToScrapper)
                .retrieve()
                .body(LinkResponse.class);
        } catch (HttpClientErrorException e) {
            log.atError()
                .setMessage("Ошибка отправки запроса на добавление подписки пользователю")
                .addKeyValue(CHAT_ID, chatId)
                .addKeyValue(URL, link)
                .addKeyValue("statusCode", e.getStatusCode().value())
                .addKeyValue("statusText", e.getStatusText())
                .log();

            throw new ApiErrorResponse(
                "Ошибка отправки запроса на добавление подписки пользователю",
                e.getStatusCode(),
                e.getStatusText(),
                e.getMessage(),
                e.getStackTrace());
        }
    }

    @Retry(name = "retryRules", fallbackMethod = "handleException")
    @CircuitBreaker(name = "cb1")
    public LinkResponse unSubscribeToLink(Long chatId, String link) throws ApiErrorResponse {
        RemoveLinkRequest requestToScrapper = new RemoveLinkRequest(link);
        try {
            return restClient
                .method(HttpMethod.DELETE)
                .uri("/links")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .body(requestToScrapper)
                .retrieve()
                .body(LinkResponse.class);
        } catch (HttpClientErrorException e) {
            log.atError()
                .setMessage("Ошибка отправки запроса на удаление подписки пользователя")
                .addKeyValue(CHAT_ID, chatId)
                .addKeyValue(URL, link)
                .addKeyValue("statusCode", e.getStatusCode().value())
                .addKeyValue("statusText", e.getStatusText())
                .log();

            throw new ApiErrorResponse(
                "Ошибка отправки запроса на удаление подписки пользователя",
                e.getStatusCode(),
                e.getStatusText(),
                e.getMessage(),
                e.getStackTrace());
        }
    }

    public void handleException(Long chatId, Throwable e) {
        throw new ApiErrorResponse(
            "Cannot connect to server",
            null,
            "",
            e.getMessage(),
            e.getStackTrace());
    }
}
