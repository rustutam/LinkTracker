package backend.academy.scrapper.models.entities;

import java.time.OffsetDateTime;

public record TagEntity(
    Long id,
    String tag,
    OffsetDateTime createdAt
) {
}
