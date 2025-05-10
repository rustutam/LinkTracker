package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkChangeStatus;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import backend.academy.scrapper.sender.LinkUpdateSender;
import dto.LinkUpdate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SenderNotificationServiceImpl implements SenderNotificationService {
    private final SubscriptionRepository subscriptionRepository;
    private final LinkUpdateSender linkUpdateSender;

    @Override
    public void notifySender(LinkChangeStatus linkChangeStatus) {
        List<Long> chatIds = findSubscribersChatIds(linkChangeStatus.link());
        if (chatIds.isEmpty()) {
            return;
        }

        LinkUpdate linkUpdate = createLinkUpdate(linkChangeStatus, chatIds);
        linkUpdateSender.sendUpdates(linkUpdate);
    }

    private List<Long> findSubscribersChatIds(Link link) {
        return subscriptionRepository.findByLink(link).stream()
            .map(subscription -> subscription.user().chatId().id())
            .toList();
    }

    private LinkUpdate createLinkUpdate(LinkChangeStatus linkChangeStatus, List<Long> chatIds) {
        String description = linkChangeStatus.changeInfoList().stream()
            .map(Object::toString)
            .collect(Collectors.joining("\n\n"));

        return new LinkUpdate(
            linkChangeStatus.link().linkId().id(),
            linkChangeStatus.link().uri().toString(),
            description,
            chatIds
        );
    }


}
