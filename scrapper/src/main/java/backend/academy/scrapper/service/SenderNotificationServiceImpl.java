package backend.academy.scrapper.service;

import backend.academy.scrapper.models.LinkMetadata;
import backend.academy.scrapper.models.LinkUpdateNotification;
import backend.academy.scrapper.repository.database.LinksRepository;
import backend.academy.scrapper.sender.Sender;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SenderNotificationServiceImpl implements SenderNotificationService {
    private final LinksRepository linkRepository;
    private final Sender sender;

    @Override
    public void notifySender(List<LinkMetadata> updatedLinks) {
        if (updatedLinks.isEmpty()) {
            return;
        }

        sender.send(linkUpdateNotificationMapper(updatedLinks));
    }

    private List<LinkUpdateNotification> linkUpdateNotificationMapper(List<LinkMetadata> updatedLinks) {
        return updatedLinks.stream()
                .map(linkMetadata -> new LinkUpdateNotification(
                        linkMetadata.id(),
                        linkMetadata.linkUri(),
                        linkRepository.getAllChatIdByLink(linkMetadata.linkUri().toString())))
                .toList();
    }
}
