package backend.academy.scrapper.models.external.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

public record Parent(
    String sha,
    URI url,
    @JsonProperty("html_url") URI htmlUrl
) {
}
