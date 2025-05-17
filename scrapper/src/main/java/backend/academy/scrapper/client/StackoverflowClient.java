package backend.academy.scrapper.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StackoverflowClient {
    private final StackoverflowRetryProxy proxy;

    public String getQuestionComments(String site, String questionId) {
        return proxy.getQuestionComments(site, questionId);
    }

    public String getQuestion(String site, String questionId) {
        return proxy.getQuestion(site, questionId);
    }

    public String getQuestionAnswers(String site, String questionId) {
        return proxy.getQuestionAnswers(site, questionId);
    }

    public String getQuestionAnswerCommits(String site, String answerId) {
        return proxy.getQuestionAnswerCommits(site, answerId);
    }
}

// Для StackOverflow новый ответ или комментарий, сообщение включает:
// текст темы вопроса
// имя пользователя
// время создания
// превью ответа или комментария (первые 200 символов)
