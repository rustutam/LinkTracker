package backend.academy.scrapper.models;

import java.util.List;

public record LinkUpdateNotification(
    String uri,
    List<Long> chatIds
) {
}
