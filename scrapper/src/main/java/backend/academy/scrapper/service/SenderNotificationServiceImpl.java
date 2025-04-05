package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.LinkUpdateNotification;
import backend.academy.scrapper.models.domain.LinkChangeStatus;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.repository.database.ChatRepository;
import backend.academy.scrapper.sender.Sender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SenderNotificationServiceImpl implements SenderNotificationService {
    private final ChatRepository chatRepository;
    private final Sender sender;

    @Override
    public void notifySender(LinkChangeStatus linkChangeStatus) {
        String description = linkChangeStatus.changeInfoList().stream()
            .map(Object::toString)
            .collect(Collectors.joining("\n"));

        LinkId linkId = linkChangeStatus.link().linkId();

        LinkUpdateNotification linkUpdateNotification = new LinkUpdateNotification(
            linkId,
            linkChangeStatus.link().uri(),
            description,
            chatRepository.findAllUsersByLinkId(linkId).stream().map(User::chatId).toList()
        );

        sender.send(linkUpdateNotification);
    }

}

