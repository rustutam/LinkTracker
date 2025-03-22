package backend.academy.scrapper.models.entities;

import java.time.OffsetDateTime;

public record UserEntity(
    Long id,
    Long chatId,
    OffsetDateTime createdAt
) {
}
