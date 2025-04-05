package backend.academy.scrapper.sender;

import backend.academy.scrapper.client.TgBotClient;
import backend.academy.scrapper.models.domain.LinkUpdateNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class BotSenderImpl extends Sender {
    private final TgBotClient tgBotClient;

    @Override
    public void send(LinkUpdateNotification linkUpdateNotification) {
        tgBotClient.send(toLinkUpdateMapper(linkUpdateNotification));
    }
}
