package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.UpdatedLink;

public interface SenderNotificationService {
    void notifySender(UpdatedLink updatedLink);
}
