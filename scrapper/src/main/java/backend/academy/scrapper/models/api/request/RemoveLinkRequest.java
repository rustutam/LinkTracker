package backend.academy.scrapper.models.api.request;

import java.net.URI;

/**
 * RemoveLinkRequest
 */
public record RemoveLinkRequest(
    URI link
) {
}

