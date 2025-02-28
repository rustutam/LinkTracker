package backend.academy.scrapper.models.api.response;

import java.net.URI;
import java.util.List;

/**
 * LinkResponse
 */
public record LinkResponse(
    Long id,
    URI url,
    List<String> tags,
    List<String> filters

) {
}

