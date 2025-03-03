package backend.academy.scrapper.client;

import backend.academy.scrapper.configuration.StackOverflowConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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



}
