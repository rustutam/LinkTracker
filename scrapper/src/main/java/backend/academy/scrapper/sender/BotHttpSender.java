package backend.academy.scrapper.sender;

import backend.academy.scrapper.client.BotClient;
import backend.academy.scrapper.configuration.ScrapperConfig;
import backend.academy.scrapper.configuration.clients.BotConfig;
import backend.academy.scrapper.exceptions.ApiErrorResponseException;
import dto.LinkUpdate;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "message-transport", havingValue = "Http")
public class BotHttpSender implements LinkUpdateSender {
    private final BotClient botClient;

    @Override
    public void sendUpdates(LinkUpdate linkUpdate) throws ApiErrorResponseException {
        botClient.sendUpdates(linkUpdate);
    }
}
