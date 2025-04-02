package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.client.StackoverflowClient;
import backend.academy.scrapper.exceptions.QuestionNotFoundException;
import backend.academy.scrapper.models.domain.ChangeInfo;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkChangeStatus;
import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StackOverflowExternalDataRepository extends ExternalDataRepository {
    private final StackoverflowClient stackoverflowClient;
    private final ObjectMapper objectMapper;
    private static final String JSON_ERROR = "Ошибка при разборе JSON-Ответа";
    private static final String REQUEST_ERROR = "Ошибка при запросе";

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

    private List<ChangeInfo> parseContentList(String jsonResponse, String url, String title) {
        List<ChangeInfo> allContent = new ArrayList<>();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            jsonNode.get("items").forEach(item -> {
                String ownerName = item.get("owner").get("display_name").asText();
                String body = item.get("body").asText();
                OffsetDateTime creationDate = OffsetDateTime.parse(item.get("creation_date").asText());
                allContent.add(new ChangeInfo(title, ownerName, creationDate, body));
            });
            return allContent;
        } catch (Exception e) {
            log.atError().addKeyValue("link", url).setMessage(JSON_ERROR).log();
            throw new HttpMessageNotReadableException("Ошибка при разборе JSON-ответа по URL " + url);
        }
    }

    public String getTitle(URI link) {
        QuestionInfo questionInfo = getQuestionInfo(link);
        String content = stackoverflowClient.getQuestion(questionInfo.site, questionInfo.questionId);
        try {
            JsonNode jsonNode = objectMapper.readTree(content);
            return jsonNode.get("items").get(0).get("title").asText();
        } catch (HttpMessageNotReadableException | JsonProcessingException e) {
            log.atError().addKeyValue("link", link.toString()).setMessage(JSON_ERROR).log();
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
        return parseContentList(questionComments, link.toString(), title);
    }

    private List<ChangeInfo> getAnswers(URI link, String title) {
        QuestionInfo questionInfo = getQuestionInfo(link);
        String questionAnswers =  stackoverflowClient.getQuestionAnswers(questionInfo.site, questionInfo.questionId);
        return parseContentList(questionAnswers, link.toString(), title);
    }

    private List<ChangeInfo> getAnswerComments(URI link, String title) {
        List<ChangeInfo> content = new ArrayList<>();
        QuestionInfo questionInfo = getQuestionInfo(link);
        String questionAnswers = stackoverflowClient.getQuestionAnswers(questionInfo.site, questionInfo.questionId);

        try {
            JsonNode jsonNode = objectMapper.readTree(questionAnswers);
            jsonNode.get("items").forEach(item -> {
                String answerId = item.get("answer_id").asText();
                String questionAnswerCommits = stackoverflowClient.getQuestionAnswerCommits(questionInfo.site, questionInfo.questionId);
                content.addAll(parseContentList(questionAnswerCommits, link.toString(), title));
            });
        } catch (HttpMessageNotReadableException | JsonProcessingException e) {
            log.atError().addKeyValue("link", link.toString()).setMessage(REQUEST_ERROR).log();
            throw new HttpMessageNotReadableException(e.getMessage());
        } catch (Exception e) {
            log.atError().addKeyValue("link", link.toString()).setMessage(REQUEST_ERROR).log();
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

    private record QuestionInfo(String site, String questionId) {
    }
}
