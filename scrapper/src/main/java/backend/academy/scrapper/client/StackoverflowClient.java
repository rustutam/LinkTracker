package backend.academy.scrapper.client;

import backend.academy.scrapper.exceptions.ApiStackOverflowErrorResponseException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StackoverflowClient {
    private final StackoverflowRetryProxy proxy;

    public Optional<String> getQuestionComments(String site, String questionId) {
        try {
            return Optional.of(proxy.getQuestionComments(site, questionId));
        } catch (ApiStackOverflowErrorResponseException ex) {
            return Optional.empty();
        }
    }

    public Optional<String> getQuestion(String site, String questionId) {
        try {
            return Optional.of(proxy.getQuestion(site, questionId));
        } catch (ApiStackOverflowErrorResponseException ex) {
            return Optional.empty();
        }
    }

    public Optional<String> getQuestionAnswers(String site, String questionId) {
        try {
            return Optional.of(proxy.getQuestionAnswers(site, questionId));
        } catch (ApiStackOverflowErrorResponseException ex) {
            return Optional.empty();
        }
    }

    public Optional<String> getQuestionAnswerCommits(String site, String answerId) {
        try {
            return Optional.of(proxy.getQuestionAnswerCommits(site, answerId));
        } catch (ApiStackOverflowErrorResponseException ex) {
            return Optional.empty();
        }
    }
}

// Для StackOverflow новый ответ или комментарий, сообщение включает:
// текст темы вопроса
// имя пользователя
// время создания
// превью ответа или комментария (первые 200 символов)
