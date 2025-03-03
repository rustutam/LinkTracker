package backend.academy.scrapper.client;

import backend.academy.scrapper.models.api.LinkUpdate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class BotClient {
    private final RestClient restClient;

    public BotClient() {
        this.restClient = RestClient.builder()
            .build();
    }

    public void send(LinkUpdate linkUpdate){
        restClient.post()
            .uri("/updates")
            .body(linkUpdate)
            .retrieve()
            .toBodilessEntity();
    }

}
