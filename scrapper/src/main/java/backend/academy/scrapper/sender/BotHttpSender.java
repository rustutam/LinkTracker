package backend.academy.scrapper.sender;

import backend.academy.scrapper.configuration.ScrapperConfig;
import backend.academy.scrapper.exceptions.ApiErrorResponseException;
import dto.LinkUpdate;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
public class BotHttpSender implements LinkUpdateSender {
    private final RestClient restClient;

    public BotHttpSender(ScrapperConfig scrapperConfig) {
        this.restClient =
                RestClient.builder().baseUrl(scrapperConfig.tgBot().baseUri()).build();
    }

    @Override
    @SuppressFBWarnings(
            value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "Когда я ловлю ошибку, она не null")
    public void sendUpdates(LinkUpdate linkUpdate) throws ApiErrorResponseException {
        try {
            restClient.post().uri("/updates").body(linkUpdate).retrieve().toBodilessEntity();

            log.atInfo()
                    .addKeyValue("link", linkUpdate.url())
                    .setMessage("Отправка обновления по HTTP")
                    .log();
        } catch (HttpClientErrorException e) {
            throw e.getResponseBodyAs(ApiErrorResponseException.class);
        }
    }
}
