package backend.academy.scrapper.client;

import backend.academy.scrapper.configuration.ScrapperConfig;
import dto.LinkUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class TgBotClient {
    private final RestClient restClient;

    public TgBotClient(ScrapperConfig scrapperConfig) {
        this.restClient = RestClient.builder().baseUrl(scrapperConfig.tgBot().baseUri()).build();
    }

    public void send(LinkUpdate linkUpdate) {
        restClient.post().uri("/updates").body(linkUpdate).retrieve().toBodilessEntity();
    }
}
