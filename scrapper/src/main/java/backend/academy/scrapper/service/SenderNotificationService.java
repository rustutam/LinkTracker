package backend.academy.scrapper.service;

import backend.academy.scrapper.models.LinkMetadata;
import java.util.List;

public interface SenderNotificationService {
    void notifySender(List<LinkMetadata> updatedLinks);
}
