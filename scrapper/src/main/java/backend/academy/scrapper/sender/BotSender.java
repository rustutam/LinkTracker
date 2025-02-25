package backend.academy.scrapper.sender;

import backend.academy.scrapper.models.dto.api.LinkUpdate;
import reactor.core.publisher.Mono;

public interface BotSender {
    Mono<Void> send(LinkUpdate linkUpdate);
}
