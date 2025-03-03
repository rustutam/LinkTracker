package backend.academy.scrapper.models;

import java.net.URI;
import java.util.List;

public record Link(
    URI uri,
    List<String> tags,
    List<String> filters
) {
}
