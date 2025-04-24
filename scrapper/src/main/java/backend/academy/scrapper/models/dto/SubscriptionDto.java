package backend.academy.scrapper.models.dto;

import java.time.OffsetDateTime;

public record SubscriptionDto(
    Long id,
    Long userId,
    Long linkId,
    OffsetDateTime createdAt
) {
}
