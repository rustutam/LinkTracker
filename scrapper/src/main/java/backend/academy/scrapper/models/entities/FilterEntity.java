package backend.academy.scrapper.models.entities;

import java.time.OffsetDateTime;

public record FilterEntity(
    Long id,
    String filter,
    OffsetDateTime createdAt
) {
}
