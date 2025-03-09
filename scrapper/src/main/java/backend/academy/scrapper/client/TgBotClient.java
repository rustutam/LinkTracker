package backend.academy.scrapper.client;

import backend.academy.scrapper.configuration.TgBotConfig;
import dto.LinkUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class TgBotClient {
    private final RestClient restClient;

    public TgBotClient(TgBotConfig tgBotConfig) {
        this.restClient = RestClient.builder()
            .baseUrl(tgBotConfig.baseUrl())
            .build();
    }

    public void send(LinkUpdate linkUpdate) {
        restClient.post()
            .uri("/updates")
            .body(linkUpdate)
            .retrieve()
            .toBodilessEntity();
    }

}
