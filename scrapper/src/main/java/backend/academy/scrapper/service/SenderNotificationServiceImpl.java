package backend.academy.scrapper.service;

import backend.academy.scrapper.models.LinkUpdateNotification;
import backend.academy.scrapper.models.domain.LinkChangeStatus;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.repository.database.ChatRepository;
import backend.academy.scrapper.sender.Sender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SenderNotificationServiceImpl implements SenderNotificationService {
    private final ChatRepository chatRepository;
    private final Sender sender;

    @Override
    public void notifySender(LinkChangeStatus linkChangeStatus) {
        sender.send(linkUpdateNotificationMapper(linkChangeStatus));
    }

    private LinkUpdateNotification linkUpdateNotificationMapper(LinkChangeStatus linkChangeStatus) {
        LinkId linkId = linkChangeStatus.link().linkId();
        return new LinkUpdateNotification(
            linkId,
            linkChangeStatus.link().uri(),
            linkChangeStatus.description(),
            chatRepository.findAllUsersByLinkId(linkId).stream().map(User::chatId).toList()
        );


    }
}
