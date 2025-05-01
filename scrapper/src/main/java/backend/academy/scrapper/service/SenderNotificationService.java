package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.LinkChangeStatus;

public interface SenderNotificationService {
    void notifySender(LinkChangeStatus linkChangeStatus);
}
