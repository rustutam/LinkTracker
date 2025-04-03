package backend.academy.scrapper.models.entities;

import java.time.OffsetDateTime;
import java.util.List;

public record LinkMetadataEntity(
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
