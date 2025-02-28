package backend.academy.scrapper.sender;

import backend.academy.scrapper.models.LinkUpdateNotification;
import backend.academy.scrapper.models.api.LinkUpdate;
import reactor.core.publisher.Mono;
import java.util.List;

public interface Sender {
    void send(List<LinkUpdateNotification> linkUpdateNotifications);
}
