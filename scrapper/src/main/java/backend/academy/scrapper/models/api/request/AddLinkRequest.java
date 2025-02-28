package backend.academy.scrapper.models.api.request;

import java.net.URI;
import java.util.List;

/**
 * AddLinkRequest
 */
public record AddLinkRequest(
    URI link,
    List<String> tags,
    List<String> filters
) {

}

