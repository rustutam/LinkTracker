package backend.academy.scrapper.sender;

import backend.academy.scrapper.models.api.LinkUpdate;
import reactor.core.publisher.Mono;

public interface BotSender {
    Mono<Void> send(LinkUpdate linkUpdate);
}
