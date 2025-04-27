package backend.academy.scrapper.models.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record SubscriptionDto(
    Long id,
    UserDto userDto,
    LinkDto linkDto,
    List<TagDto> tagsDto,
    List<FilterDto> filtersDto,
    OffsetDateTime createdAt
) {
}
