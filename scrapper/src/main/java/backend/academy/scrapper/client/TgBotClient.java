package backend.academy.scrapper.client;

import backend.academy.scrapper.configuration.TgBotConfig;
import backend.academy.scrapper.models.api.LinkUpdate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TgBotClient {
    private final RestClient restClient;

    public TgBotClient(TgBotConfig tgBotConfig) {
        this.restClient = RestClient.builder()
            .baseUrl(tgBotConfig.baseUrl())
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
