package backend.academy.scrapper.sender;

import dto.LinkUpdate;

public interface LinkUpdateSender {
    void pushLinkUpdate(LinkUpdate linkUpdate);
}
