package backend.academy.scrapper.client;

import backend.academy.scrapper.configuration.StackOverflowConfig;
import backend.academy.scrapper.models.external.stackoverflow.StackoverflowQuestionDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class StackoverflowClient {
    private final RestClient restClient;

    public StackoverflowClient(StackOverflowConfig stackoverflowConfig) {
        restClient = RestClient.builder()
            .baseUrl(stackoverflowConfig.baseUrl())
            .defaultHeader("key", stackoverflowConfig.key())
            .defaultHeader("access_token", stackoverflowConfig.accessToken())
            .build();
    }

    public StackoverflowQuestionDTO questionRequest(String questionId) {
        return restClient
            .get()
            .uri("/questions/{questionId}/?site=stackoverflow&filter=withbody", questionId)
            .retrieve()
            .body(StackoverflowQuestionDTO.class);
    }


}
