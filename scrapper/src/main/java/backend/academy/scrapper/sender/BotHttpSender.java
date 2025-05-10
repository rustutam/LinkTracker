package backend.academy.scrapper.sender;


import backend.academy.scrapper.configuration.ScrapperConfig;
import backend.academy.scrapper.exceptions.ApiErrorResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.LinkUpdate;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Mono;

@Slf4j
public class BotHttpSender implements LinkUpdateSender {
    private final RestClient restClient;

    public BotHttpSender(ScrapperConfig scrapperConfig) {
        this.restClient = RestClient.builder()
            .baseUrl(scrapperConfig.tgBot().baseUri())
            .build();
    }

    @Override
    @SuppressFBWarnings(
        value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
        justification = "Когда я ловлю ошибку, она не null")
    public void sendUpdates(LinkUpdate linkUpdate) throws ApiErrorResponseException {
        log.info("sending an update {}", linkUpdate);
        try {
            restClient
                .post()
                .uri("/updates")
                .body(linkUpdate)
                .retrieve()
                .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            throw e.getResponseBodyAs(ApiErrorResponseException.class);
        }
    }

}
