package backend.academy.scrapper.models.api.request;

import jakarta.validation.constraints.NotNull;
import java.net.URI;

/**
 * RemoveLinkRequest
 */
public record RemoveLinkRequest(
    @NotNull URI link
) {
}

