package backend.academy.scrapper.models.entities;

import java.time.OffsetDateTime;

public record LinkEntity(
    Long id,
    String uri,
    Long tagId,
    Long filterId,
    OffsetDateTime lastModifiedDate,
    OffsetDateTime createdAt
) {
}
