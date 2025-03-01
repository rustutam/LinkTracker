package backend.academy.scrapper.models;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public record Link(
    URI url,
    List<String> tags,
    List<String> filters
) {
}
