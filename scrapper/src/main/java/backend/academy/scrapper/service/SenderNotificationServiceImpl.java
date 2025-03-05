package backend.academy.scrapper.service;

import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.models.LinkUpdateNotification;
import backend.academy.scrapper.repository.database.LinksRepository;
import backend.academy.scrapper.sender.Sender;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SenderNotificationServiceImpl implements SenderNotificationService {
    private final LinksRepository linkRepository;
    private final Sender sender;

    public void notifySender(List<Link> updatedLinks) {
        sender.send(linkUpdateNotificationMapper(updatedLinks));
    }

    private List<LinkUpdateNotification> linkUpdateNotificationMapper(List<Link> updatedLinks) {
        List<LinkUpdateNotification> linkUpdateNotifications = new ArrayList<>();

        for (Link linkInfo : updatedLinks) {
            String link = linkInfo.uri().toString();
            List<Long> allChatIds = linkRepository.getAllChatIdByLink(link);
            linkUpdateNotifications.add(
                new LinkUpdateNotification(link, allChatIds)
            );
        }
        return linkUpdateNotifications;
    }
}
