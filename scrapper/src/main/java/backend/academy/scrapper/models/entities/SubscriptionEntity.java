package backend.academy.scrapper.models.entities;

import java.time.OffsetDateTime;

public record SubscriptionEntity(
    Long id,
    Long userId,
    Long linkId,
    OffsetDateTime createdAt
) {
}
