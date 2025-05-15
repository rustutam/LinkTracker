package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.LinkChangeStatus;
import backend.academy.scrapper.models.domain.LinkUpdateNotification;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import backend.academy.scrapper.sender.Sender;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SenderNotificationServiceImpl implements SenderNotificationService {
    private final SubscriptionRepository subscriptionRepository;
    private final Sender sender;

    @Override
    public void notifySender(LinkChangeStatus linkChangeStatus) {
        String description =
                linkChangeStatus.changeInfoList().stream().map(Object::toString).collect(Collectors.joining("\n\n"));

        LinkId linkId = linkChangeStatus.link().linkId();

        List<ChatId> chatIds = subscriptionRepository.findByLink(linkChangeStatus.link()).stream()
                .map(subscription -> subscription.user().chatId())
                .toList();

        LinkUpdateNotification linkUpdateNotification =
                new LinkUpdateNotification(linkId, linkChangeStatus.link().uri(), description, chatIds);

        sender.send(linkUpdateNotification);
    }
}
