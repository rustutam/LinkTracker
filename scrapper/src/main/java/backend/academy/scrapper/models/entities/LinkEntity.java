package backend.academy.scrapper.models.entities;

import java.time.OffsetDateTime;

public record LinkEntity(
    Long id,
    String uri,
    OffsetDateTime lastModifiedDate,
    OffsetDateTime createdAt
) {
}
