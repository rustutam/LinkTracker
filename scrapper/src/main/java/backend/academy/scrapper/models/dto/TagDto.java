package backend.academy.scrapper.models.dto;

import java.time.OffsetDateTime;

public record TagDto(
    Long id,
    String value,
    OffsetDateTime createdAt
) {
}
