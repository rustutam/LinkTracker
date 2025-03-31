package backend.academy.scrapper.models;

import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import java.net.URI;
import java.util.List;

public record LinkUpdateNotification(
    LinkId linkId,
    URI uri,
    String description,
    List<ChatId> chatIds
) {}
