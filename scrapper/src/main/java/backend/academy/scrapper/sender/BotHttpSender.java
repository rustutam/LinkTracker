package backend.academy.scrapper.sender;


import backend.academy.scrapper.configuration.ScrapperConfig;
import dto.LinkUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class BotHttpSender implements LinkUpdateSender {
    private final RestClient restClient;

    public BotHttpSender(ScrapperConfig scrapperConfig) {
        this.restClient = RestClient.builder()
            .baseUrl(scrapperConfig.tgBot().baseUri())
            .build();
    }

    @Override
    public void pushLinkUpdate(LinkUpdate linkUpdate) {
        log.info("sending an update {}", linkUpdate);
        restClient
            .post()
            .uri("/updates")
            .body(linkUpdate)
            .retrieve()
            .toBodilessEntity();
    }

}
