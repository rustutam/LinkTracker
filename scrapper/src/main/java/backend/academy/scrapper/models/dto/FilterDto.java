package backend.academy.scrapper.models.dto;

import java.time.OffsetDateTime;

public record FilterDto(
    Long id,
    String value,
    OffsetDateTime createdAt
) {
}
