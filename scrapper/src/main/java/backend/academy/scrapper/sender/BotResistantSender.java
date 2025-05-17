package backend.academy.scrapper.sender;

import backend.academy.scrapper.exceptions.ApiErrorResponseException;
import dto.LinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class BotResistantSender implements LinkUpdateSender {
    private final ResistantProxy proxy;

    @Override
    public void sendUpdates(LinkUpdate linkUpdate) throws ApiErrorResponseException {
        proxy.sendUpdates(linkUpdate);
    }
}
