package backend.academy.scrapper.models.dto;

import java.time.OffsetDateTime;

public record UserDto(
    Long id,
    Long chatId,
    OffsetDateTime createdAt
) {
}
