package backend.academy.scrapper.client;

import dto.LinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BotClient {
    private final BotRetryProxy proxy;

    public void sendUpdates(LinkUpdate linkUpdate) {
        proxy.sendUpdates(linkUpdate);
    }
}
