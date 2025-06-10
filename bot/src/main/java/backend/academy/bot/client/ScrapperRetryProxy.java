package backend.academy.bot.client;

import backend.academy.bot.api.dto.AddLinkRequest;
import backend.academy.bot.api.dto.ApiErrorResponse;
import backend.academy.bot.api.dto.LinkResponse;
import backend.academy.bot.api.dto.ListLinksResponse;
import backend.academy.bot.api.dto.RemoveLinkRequest;
import backend.academy.bot.config.clients.ScrapperConfig;
import backend.academy.bot.exceptions.ApiScrapperErrorResponseException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import general.ExceptionUtils;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import static backend.academy.bot.utils.LogMessages.CHAT_ID;
import static backend.academy.bot.utils.LogMessages.URL;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class ScrapperRetryProxy {
    private final ExceptionUtils exceptionUtils;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public ScrapperRetryProxy(ExceptionUtils exceptionUtils, ScrapperConfig scrapperConfig, ObjectMapper objectMapper) {
        this.exceptionUtils = exceptionUtils;
        this.restClient = scrapperConfig.restClient();
        this.objectMapper = objectMapper;
    }

    @SuppressFBWarnings(
        value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
        justification = "Я уверен, что когда я ловлю ошибку, она не является null")
    @Retry(name = "retryRules")
    @CircuitBreaker(name = "cb1")
    public void registerChat(Long chatId) throws ApiScrapperErrorResponseException {
        try {
            restClient.post().uri("/tg-chat/{id}", chatId).retrieve().toBodilessEntity();
        } catch (HttpClientErrorException e) {
            log.atError()
                .setMessage("Ошибка отправки запроса на регистрацию чата")
                .addKeyValue(CHAT_ID, chatId)
                .addKeyValue("statusCode", e.getStatusCode().value())
                .addKeyValue("statusText", e.getStatusText())
                .log();
            throw buildException(
                "Ошибка отправки запроса на регистрацию чата",
                e
            );
        }
    }
    @SuppressFBWarnings(
        value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
        justification = "Я уверен, что когда я ловлю ошибку, она не является null")
    @Retry(name = "retryRules")
    @CircuitBreaker(name = "cb1")
    public void deleteChat(Long chatId) throws ApiScrapperErrorResponseException {
        try {
            restClient.delete().uri("/tg-chat/{id}", chatId).retrieve().toBodilessEntity();
        } catch (HttpClientErrorException e) {
            log.atError()
                .setMessage("Ошибка отправки запроса на удаление чата")
                .addKeyValue(CHAT_ID, chatId)
                .addKeyValue("statusCode", e.getStatusCode().value())
                .addKeyValue("statusText", e.getStatusText())
                .log();

            throw buildException(
                "Ошибка отправки запроса на удаление чата",
                e
            );
        }
    }
    @SuppressFBWarnings(
        value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
        justification = "Я уверен, что когда я ловлю ошибку, она не является null")
    @Retry(name = "retryRules")
    @CircuitBreaker(name = "cb1")
    public ListLinksResponse getLinks(Long chatId) throws ApiScrapperErrorResponseException {
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

            throw buildException(
                "Ошибка отправки запроса на получение всех ссылок пользователя",
                e
            );
        }
    }
    @SuppressFBWarnings(
        value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
        justification = "Я уверен, что когда я ловлю ошибку, она не является null")
//    @Retry(name = "retryRules")
//    @CircuitBreaker(name = "cb1")
    public LinkResponse subscribeToLink(Long chatId, String link, List<String> tags, List<String> filters)
        throws ApiScrapperErrorResponseException {
        AddLinkRequest requestToScrapper = new AddLinkRequest(link, tags, filters);
        try {
            return restClient
                .post()
                .uri("/links")
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

            throw buildException(
                "Ошибка отправки запроса на добавление подписки пользователю",
                e
            );
        }
    }
    @SuppressFBWarnings(
        value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
        justification = "Я уверен, что когда я ловлю ошибку, она не является null")
    @Retry(name = "retryRules")
    @CircuitBreaker(name = "cb1")
    public LinkResponse unSubscribeToLink(Long chatId, String link) throws ApiScrapperErrorResponseException {
        RemoveLinkRequest requestToScrapper = new RemoveLinkRequest(link);
        try {
            return restClient
                .method(HttpMethod.DELETE)
                .uri("/links")
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
            throw buildException(
                "Ошибка отправки запроса на удаление подписки пользователя",
                e
            );
        }
    }

    private ApiScrapperErrorResponseException buildException(String message, HttpClientErrorException e) {
        try {
            String body = e.getResponseBodyAsString();
            ApiErrorResponse errorResponse = objectMapper.readValue(body, ApiErrorResponse.class);

            return new ApiScrapperErrorResponseException(
                errorResponse.description(),
                errorResponse.code(),
                errorResponse.exceptionName(),
                errorResponse.exceptionMessage(),
                errorResponse.stacktrace()
            );
        } catch (Exception ex) {
            return new ApiScrapperErrorResponseException(
                message,
                String.valueOf(e.getStatusCode().value()),
                e.getStatusText(),
                e.getMessage(),
                exceptionUtils.getStacktrace(ex)
            );
        }
    }
}
