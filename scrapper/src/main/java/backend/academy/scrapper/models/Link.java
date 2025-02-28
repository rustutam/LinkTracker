package backend.academy.scrapper.models;

import java.time.OffsetDateTime;
import java.util.List;

public record Link(
    long id,
    String url,
    List<String> tags,
    List<String> filters,
    OffsetDateTime lastUpdate
) {
}
