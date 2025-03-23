package backend.academy.scrapper.models.domain;

import backend.academy.scrapper.models.domain.ids.LinkId;
import java.net.URI;
import java.time.OffsetDateTime;

public record Link(
    LinkId linkId,
    URI uri,
    OffsetDateTime lastUpdateTime
) {
}
