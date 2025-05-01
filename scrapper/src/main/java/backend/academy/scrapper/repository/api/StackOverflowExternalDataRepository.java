package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.client.StackoverflowClient;
import backend.academy.scrapper.exceptions.QuestionNotFoundException;
import backend.academy.scrapper.models.domain.ChangeInfo;
import backend.academy.scrapper.models.domain.Link;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StackOverflowExternalDataRepository extends ExternalDataRepository {
    private static final String QUESTION_ANSWERS_DESCRIPTION = "Новый ответ на вопрос";
    private static final String QUESTION_COMMENTS_DESCRIPTION = "Новые комментарии к вопросу";
    private static final String ANSWER_COMMENTS_DESCRIPTION = "Новые комментарии к ответу";

    private static final String JSON_ERROR = "Ошибка при разборе JSON-Ответа";
    private static final String REQUEST_ERROR = "Ошибка при запросе";
    public static final String ITEMS = "items";
    private final StackoverflowClient stackoverflowClient;
    private final ObjectMapper objectMapper;

    @Override
    public List<ChangeInfo> getChangeInfoByLink(Link link) {
        URI uri = link.uri();
        String title = getTitle(uri);

        List<ChangeInfo> allContent = new ArrayList<>();
        allContent.addAll(getComments(uri, title));
        allContent.addAll(getAnswers(uri, title));
        allContent.addAll(getAnswerComments(uri, title));

        return allContent;
    }

    private List<ChangeInfo> parseContentList(String description, String jsonResponse, String url, String title) {
        List<ChangeInfo> allContent = new ArrayList<>();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            jsonNode.get(ITEMS).forEach(item -> {
                String ownerName = item.get("owner").get("display_name").asText();
                String preview = truncatePreview(item.get("body").asText());
                OffsetDateTime creationDate =
                        OffsetDateTime.parse(item.get("creation_date").asText());
                allContent.add(new ChangeInfo(description, title, ownerName, creationDate, preview));
            });
            return allContent;
        } catch (JsonProcessingException e) {
            log.atError().addKeyValue("link", url).setMessage(JSON_ERROR).log();
            throw new HttpMessageNotReadableException("Ошибка парсинга JSON по URL " + url, e);
        } catch (NullPointerException e) {
            log.atError()
                    .addKeyValue("link", url)
                    .setMessage("Некорректная структура JSON")
                    .log();
            throw new HttpMessageNotReadableException("Некорректная структура JSON-ответа", e);
        }
    }

    public String getTitle(URI link) {
        QuestionInfo questionInfo = getQuestionInfo(link);
        String content = stackoverflowClient.getQuestion(questionInfo.site, questionInfo.questionId);
        try {
            JsonNode jsonNode = objectMapper.readTree(content);
            return jsonNode.get(ITEMS).get(0).get("title").asText();
        } catch (HttpMessageNotReadableException | JsonProcessingException e) {
            log.atError()
                    .addKeyValue("link", link.toString())
                    .setMessage(JSON_ERROR)
                    .log();
            throw new HttpMessageNotReadableException("Ошибка при разборе JSON-ответа по URL " + link.toString());
        } catch (Exception e) {
            log.atError()
                    .addKeyValue("link", link.toString())
                    .setMessage("Некорректное тело запроса")
                    .log();
            throw new RuntimeException("Некорректное тело запроса по URL " + link.toString());
        }
    }

    private List<ChangeInfo> getComments(URI link, String title) {
        QuestionInfo questionInfo = getQuestionInfo(link);
        String questionComments = stackoverflowClient.getQuestionComments(questionInfo.site, questionInfo.questionId);
        return parseContentList(QUESTION_COMMENTS_DESCRIPTION, questionComments, link.toString(), title);
    }

    private List<ChangeInfo> getAnswers(URI link, String title) {
        QuestionInfo questionInfo = getQuestionInfo(link);
        String questionAnswers = stackoverflowClient.getQuestionAnswers(questionInfo.site, questionInfo.questionId);
        return parseContentList(QUESTION_ANSWERS_DESCRIPTION, questionAnswers, link.toString(), title);
    }

    private List<ChangeInfo> getAnswerComments(URI link, String title) {
        List<ChangeInfo> content = new ArrayList<>();
        QuestionInfo questionInfo = getQuestionInfo(link);
        String questionAnswers = stackoverflowClient.getQuestionAnswers(questionInfo.site, questionInfo.questionId);

        try {
            JsonNode jsonNode = objectMapper.readTree(questionAnswers);
            jsonNode.get(ITEMS).forEach(item -> {
                String answerId = item.get("answer_id").asText();
                String questionAnswerCommits =
                        stackoverflowClient.getQuestionAnswerCommits(questionInfo.site, questionInfo.questionId);
                content.addAll(
                        parseContentList(ANSWER_COMMENTS_DESCRIPTION, questionAnswerCommits, link.toString(), title));
            });
        } catch (HttpMessageNotReadableException | JsonProcessingException e) {
            log.atError()
                    .addKeyValue("link", link.toString())
                    .setMessage(REQUEST_ERROR)
                    .log();
            throw new HttpMessageNotReadableException(e.getMessage());
        } catch (Exception e) {
            log.atError()
                    .addKeyValue("link", link.toString())
                    .setMessage(REQUEST_ERROR)
                    .log();
            throw new QuestionNotFoundException(e.getMessage());
        }

        return content;
    }

    private OffsetDateTime getOffsetDateTime(Integer lastActivityDateInSeconds) {
        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(lastActivityDateInSeconds), ZoneOffset.UTC);
    }

    @Override
    protected boolean isProcessingUri(URI uri) {
        return uri.getHost().equals("stackoverflow.com");
    }

    private QuestionInfo getQuestionInfo(URI url) {
        String[] parts = url.getPath().split("/");
        String host = url.getHost();
        return new QuestionInfo(host, parts[2]);
    }

    private record QuestionInfo(String site, String questionId) {}
}
