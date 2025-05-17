package backend.academy.scrapper.client;

import dto.LinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "message-transport", havingValue = "Http")
public class BotClient {
    private final BotRetryProxy proxy;

    public void sendUpdates(LinkUpdate linkUpdate) {
        proxy.sendUpdates(linkUpdate);
    }

}
