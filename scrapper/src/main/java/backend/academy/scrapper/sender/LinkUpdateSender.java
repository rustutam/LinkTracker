package backend.academy.scrapper.sender;

import backend.academy.scrapper.exceptions.ApiErrorResponseException;
import dto.LinkUpdate;

public interface LinkUpdateSender {
    void sendUpdates(LinkUpdate linkUpdate) throws ApiErrorResponseException;
}
