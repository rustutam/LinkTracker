package backend.academy.scrapper.service;

import backend.academy.scrapper.models.LinkInfo;
import java.util.List;

public interface SenderNotificationService {
    void notifySender(List<LinkInfo> updatedLinks);
}
