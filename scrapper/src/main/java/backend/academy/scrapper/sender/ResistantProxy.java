package backend.academy.scrapper.sender;

import backend.academy.scrapper.exceptions.ApiBotErrorResponseException;
import dto.LinkUpdate;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResistantProxy {
    private final BotKafkaSender kafka;
    private final BotHttpSender http;

    @Retry(name = "resistantLinkTracker", fallbackMethod = "sendUpdatesKafka")
    public void sendUpdates(LinkUpdate linkUpdate) throws ApiBotErrorResponseException {
        http.sendUpdates(linkUpdate);
    }

    public void sendUpdatesKafka(LinkUpdate linkUpdate, Exception ex) {
        kafka.sendUpdates(linkUpdate);
    }
}
