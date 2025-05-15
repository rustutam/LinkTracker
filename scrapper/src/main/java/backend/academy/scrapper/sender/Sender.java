package backend.academy.scrapper.sender;

import backend.academy.scrapper.models.domain.LinkUpdateNotification;
import backend.academy.scrapper.models.domain.ids.ChatId;
import dto.LinkUpdate;

public abstract class Sender {
    public abstract void send(LinkUpdateNotification linkUpdateNotification);

    protected LinkUpdate toLinkUpdateMapper(LinkUpdateNotification linkUpdateNotification) {
        return new LinkUpdate(
                linkUpdateNotification.linkId().id(),
                linkUpdateNotification.uri().toString(),
                linkUpdateNotification.description(),
                linkUpdateNotification.chatIds().stream().map(ChatId::id).toList());
    }
}
