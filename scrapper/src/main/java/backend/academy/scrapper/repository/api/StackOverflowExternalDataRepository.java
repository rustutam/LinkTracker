package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.client.StackoverflowClient;
import backend.academy.scrapper.models.domain.ChangeInfo;
import backend.academy.scrapper.models.domain.Link;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StackOverflowExternalDataRepository extends ExternalDataRepository {
    private static final String QUESTION_ANSWERS_DESCRIPTION = "Новый ответ на вопрос";
    private static final String QUESTION_COMMENTS_DESCRIPTION = "Новые комментарии к вопросу";
    private static final String ANSWER_COMMENTS_DESCRIPTION = "Новые комментарии к ответу";
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
            JsonNode jsonNode = objectMapper.readTree(jsonResponse).get(ITEMS);

            jsonNode.forEach(content -> {
                ChangeInfo changeInfo = ChangeInfo.builder()
                        .description(description)
                        .title(title)
                        .username(content.get("user").get("display_name").asText())
                        .creationTime(OffsetDateTime.parse(
                                content.get("creation_date").asText()))
                        .preview(truncatePreview(content.get("body").asText()))
                        .build();

                allContent.add(changeInfo);
            });
            return allContent;
        } catch (JsonProcessingException e) {
            log.atError()
                    .addKeyValue("link", url)
                    .setMessage("Ошибка при обработке stackoverflow контента")
                    .setCause(e)
                    .log();
            return List.of();
        }
    }

    private String getTitle(URI link) {
        QuestionInfo questionInfo = getQuestionInfo(link);
        return stackoverflowClient
                .getQuestion(questionInfo.site, questionInfo.questionId)
                .map(response -> parseTitle(response, link))
                .orElse("-");
    }

    private String parseTitle(String response, URI link) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get(ITEMS).get(0).get("title").asText();
        } catch (JsonProcessingException e) {
            log.atError()
                    .addKeyValue("link", link)
                    .setMessage("Ошибка при получении stackoverflow title")
                    .setCause(e)
                    .log();

            return "-";
        }
    }

    private List<ChangeInfo> getComments(URI link, String title) {
        QuestionInfo questionInfo = getQuestionInfo(link);
        return stackoverflowClient
                .getQuestionComments(questionInfo.site, questionInfo.questionId)
                .map(response -> parseContentList(QUESTION_COMMENTS_DESCRIPTION, response, link.toString(), title))
                .orElse(List.of());
    }

    private List<ChangeInfo> getAnswers(URI link, String title) {
        QuestionInfo questionInfo = getQuestionInfo(link);
        return stackoverflowClient
                .getQuestionAnswers(questionInfo.site, questionInfo.questionId)
                .map(response -> parseContentList(QUESTION_ANSWERS_DESCRIPTION, response, link.toString(), title))
                .orElse(List.of());
    }

    private List<ChangeInfo> getAnswerComments(URI link, String title) {
        QuestionInfo questionInfo = getQuestionInfo(link);
        return stackoverflowClient
                .getQuestionAnswers(questionInfo.site, questionInfo.questionId)
                .map(response -> parseAnswerComments(questionInfo, response, link, title))
                .orElse(List.of());
    }

    private List<ChangeInfo> parseAnswerComments(
            QuestionInfo questionInfo, String jsonResponse, URI link, String title) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResponse).get(ITEMS);

            if (jsonNode == null || !jsonNode.isArray()) {
                return List.of();
            }

            return StreamSupport.stream(jsonNode.spliterator(), false)
                    .map(item ->
                            stackoverflowClient.getQuestionAnswerCommits(questionInfo.site, questionInfo.questionId))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    // Парсим список ChangeInfo из каждого JSON‑ответа и «расплющиваем» результат
                    .flatMap(response ->
                            parseContentList(ANSWER_COMMENTS_DESCRIPTION, response, link.toString(), title).stream())
                    .collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            log.atError()
                    .addKeyValue("link", link.toString())
                    .setMessage("Ошибка при обработке stackoverflow контента")
                    .setCause(e)
                    .log();
            return List.of();
        }
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
