package backend.academy.scrapper.client;

import backend.academy.scrapper.configuration.StackOverflowConfig;
import backend.academy.scrapper.exceptions.QuestionNotFoundException;
import backend.academy.scrapper.models.external.stackoverflow.StackoverflowQuestionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
@Slf4j
public class StackoverflowClient {
    private final RestClient restClient;
    private final StackOverflowConfig stackOverflowConfig;
    private final String filter = "withbody";
    private static final String REQUEST_ERROR = "Ошибка при запросе";

    public StackoverflowClient(StackOverflowConfig stackOverflowConfig) {
        restClient = RestClient.builder().baseUrl(stackOverflowConfig.baseUrl()).build();
        this.stackOverflowConfig = stackOverflowConfig;
    }

    public String getQuestionComments(String site, String questionId) {
        String content = String.format("/questions/%s/comments?site=%s", questionId, site);
        return performRequest(content);
    }

    public String getQuestion(String site, String questionId) {
        String content = String.format("/questions/%s?site=%s", questionId, site);
        return performRequest(content);
    }

    public String getQuestionAnswers(String site, String questionId) {
        String content = String.format("/questions/%s/answers?site=%s", questionId, site);
        return performRequest(content);
    }

    public String getQuestionAnswerCommits(String site, String answerId) {
        String content = String.format("https://api.stackexchange.com/2.3/answers/%s/comments?site=%s", answerId, site);
        return performRequest(content);
    }


    private String performRequest(String url) {
        url += "&key=" + stackOverflowConfig.key() + "&access_token=" + stackOverflowConfig.accessToken() + "&filter=" + filter;

        try {
            return restClient.get().uri(url).retrieve().body(String.class);
        } catch (RestClientResponseException e) {
            log.atError().addKeyValue("link", url).setMessage(REQUEST_ERROR).log();
            throw new QuestionNotFoundException("Ошибка при запросе: " + url);
        }
    }
}

//Для StackOverflow новый ответ или комментарий, сообщение включает:
//текст темы вопроса
//имя пользователя
//время создания
//превью ответа или комментария (первые 200 символов)
