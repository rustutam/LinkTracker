package backend.academy.scrapper.proxies;

import edu.java.models.dto.StackoverflowAnswersDTO;
import edu.java.models.dto.StackoverflowQuestionDTO;
import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackoverflowProxy {
    private static final String STACKOVERFLOW_BASE_URI = "https://api.stackexchange.com/2.3";

    private final WebClient stackoverflowClient;

    public StackoverflowProxy(WebClient.Builder webClientBuilder, String baseUri) {
        this.stackoverflowClient = webClientBuilder
            .baseUrl(Objects.requireNonNullElse(baseUri, STACKOVERFLOW_BASE_URI))
            .build();
    }

    public Mono<StackoverflowQuestionDTO> getQuestionRequest(String questionId) {
        return this.stackoverflowClient
            .get()
            .uri("/questions/{questionId}/?site=stackoverflow&filter=withbody", questionId)
            .retrieve()
            .bodyToMono(StackoverflowQuestionDTO.class);
    }

    public Mono<StackoverflowAnswersDTO> getAnswersForQuestion(String questionId) {
        return this.stackoverflowClient
            .get()
            .uri("/questions/{questionId}/answers?site=stackoverflow&filter=withbody", questionId)
            .retrieve()
            .bodyToMono(StackoverflowAnswersDTO.class);
    }

}
