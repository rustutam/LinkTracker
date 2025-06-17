package backend.academy.scrapper.sender;

import backend.academy.scrapper.exceptions.ApiBotErrorResponseException;
import dto.LinkUpdate;

public interface LinkUpdateSender {
    void sendUpdates(LinkUpdate linkUpdate) throws ApiBotErrorResponseException;
}
