package backend.academy.scrapper.models.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record LinkMetadataDto(
    long subscriptionId,
    long linkId,
    String linkUri,
    OffsetDateTime linkLastModifiedDate,
    List<Long> tagIds,
    List<String> tagNames,
    List<Long> filterIds,
    List<String> filterNames
) {
}
