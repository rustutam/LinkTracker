package backend.academy.scrapper.sender;

import backend.academy.scrapper.models.LinkUpdateNotification;
import java.util.List;

public interface Sender {
    void send(List<LinkUpdateNotification> linkUpdateNotifications);
}
