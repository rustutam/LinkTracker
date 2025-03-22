package backend.academy.scrapper.models.entities;

import java.time.OffsetDateTime;

public record SubscriptionEntity(
    Long userId,
    Long linkId,
    OffsetDateTime subscribedAt
) {
}
