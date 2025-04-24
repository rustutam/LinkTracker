package backend.academy.scrapper.models.dto;

import java.time.OffsetDateTime;

public record LinkDto(
    Long id,
    String uri,
    OffsetDateTime lastModifiedDate,
    OffsetDateTime createdAt
) {
}
