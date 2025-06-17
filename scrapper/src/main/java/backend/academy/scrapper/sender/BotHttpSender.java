package backend.academy.scrapper.sender;

import backend.academy.scrapper.client.BotClient;
import backend.academy.scrapper.exceptions.ApiBotErrorResponseException;
import dto.LinkUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotHttpSender implements LinkUpdateSender {
    private final BotClient botClient;

    @Override
    public void sendUpdates(LinkUpdate linkUpdate) throws ApiBotErrorResponseException {
        botClient.sendUpdates(linkUpdate);
    }
}
