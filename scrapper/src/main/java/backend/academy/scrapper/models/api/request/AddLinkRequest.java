package backend.academy.scrapper.models.api.request;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

/**
 * AddLinkRequest
 */
public record AddLinkRequest(
    @NotNull URI link,
    @NotNull List<String> tags,
    @NotNull List<String> filters
) {

}

