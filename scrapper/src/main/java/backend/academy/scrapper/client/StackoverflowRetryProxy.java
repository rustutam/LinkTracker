package backend.academy.scrapper.client;

import backend.academy.scrapper.configuration.clients.StackOverflowConfig;
import backend.academy.scrapper.exceptions.ApiStackOverflowErrorResponseException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class StackoverflowRetryProxy {
    private static final String WITH_BODY = "withbody";
    public static final String SO_API_CLIENT_ERROR_RESPONSE_MESSAGE = "Ошибка получения ответа от API Stackoverflow";

    private final RestClient restClient;
    private final String key;
    private final String token;
    private final Integer pageSize;

    public StackoverflowRetryProxy(StackOverflowConfig stackOverflowConfig) {
        restClient = stackOverflowConfig.stRestClient();
        key = stackOverflowConfig.key();
        token = stackOverflowConfig.accessToken();
        pageSize = stackOverflowConfig.pageSize();
    }

    @Retry(name = "so")
    @CircuitBreaker(name = "cb1")
    public String getQuestionComments(String site, String questionId) throws ApiStackOverflowErrorResponseException {
        String content = String.format("/questions/%s/comments?site=%s", questionId, site);
        return performRequest(content);
    }

    @Retry(name = "so")
    @CircuitBreaker(name = "cb1")
    public String getQuestion(String site, String questionId) throws ApiStackOverflowErrorResponseException {
        String content = String.format("/questions/%s?site=%s", questionId, site);
        return performRequest(content);
    }

    @Retry(name = "so")
    @CircuitBreaker(name = "cb1")
    public String getQuestionAnswers(String site, String questionId) throws ApiStackOverflowErrorResponseException {
        String content = String.format("/questions/%s/answers?site=%s", questionId, site);
        return performRequest(content);
    }

    @Retry(name = "so")
    @CircuitBreaker(name = "cb1")
    public String getQuestionAnswerCommits(String site, String answerId) throws ApiStackOverflowErrorResponseException {
        String content = String.format("/answers/%s/comments?site=%s", answerId, site);
        return performRequest(content);
    }

    private String performRequest(String url) {
        url += "&key=" + key + "&access_token=" + token + "&filter=" + WITH_BODY;

        try {
            return restClient.get().uri(url).retrieve().body(String.class);
        } catch (HttpClientErrorException e) {
            log.atError()
                .setMessage(SO_API_CLIENT_ERROR_RESPONSE_MESSAGE)
                .addKeyValue("link", url)
                .addKeyValue("statusCode", e.getStatusCode().value())
                .addKeyValue("statusText", e.getStatusText())
                .log();

            throw new ApiStackOverflowErrorResponseException(
                SO_API_CLIENT_ERROR_RESPONSE_MESSAGE,
                e.getStatusCode(),
                e.getStatusText(),
                e.getMessage(),
                e.getStackTrace()
            );
        }
    }
}
