package backend.academy.scrapper.client;

import backend.academy.scrapper.configuration.clients.StackOverflowConfig;
import backend.academy.scrapper.exceptions.QuestionNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Slf4j
@Service
public class StackoverflowRetryProxy {
    private static final String WITH_BODY = "withbody";
    private static final String REQUEST_ERROR = "Ошибка при запросе";

    private final RestClient restClient;
    private final String key;
    private final String token;

    public StackoverflowRetryProxy(StackOverflowConfig stackOverflowConfig) {
        restClient = stackOverflowConfig.stRestClient();
        key = stackOverflowConfig.key();
        token = stackOverflowConfig.accessToken();
    }

    @Retry(name = "so")
    @CircuitBreaker(name = "cb1")
    public String getQuestionComments(String site, String questionId) {
        String content = String.format("/questions/%s/comments?site=%s", questionId, site);
        return performRequest(content);
    }

    @Retry(name = "so")
    @CircuitBreaker(name = "cb1")
    public String getQuestion(String site, String questionId) {
        String content = String.format("/questions/%s?site=%s", questionId, site);
        return performRequest(content);
    }

    @Retry(name = "so")
    @CircuitBreaker(name = "cb1")
    public String getQuestionAnswers(String site, String questionId) {
        String content = String.format("/questions/%s/answers?site=%s", questionId, site);
        return performRequest(content);
    }

    @Retry(name = "so")
    @CircuitBreaker(name = "cb1")
    public String getQuestionAnswerCommits(String site, String answerId) {
        String content = String.format("/answers/%s/comments?site=%s", answerId, site);
        return performRequest(content);
    }

    private String performRequest(String url) {
        url += "&key=" + key + "&access_token=" + token + "&filter=" + WITH_BODY;

        try {
            return restClient.get().uri(url).retrieve().body(String.class);
        } catch (RestClientResponseException e) {
            log.atError().addKeyValue("link", url).setMessage(REQUEST_ERROR).log();
            throw new QuestionNotFoundException("Ошибка при запросе: " + url);
        }
    }
}
