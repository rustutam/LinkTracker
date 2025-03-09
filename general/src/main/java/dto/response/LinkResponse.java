package dto.response;

import java.net.URI;
import java.util.List;

/**
 * LinkResponse
 */
public record LinkResponse(
    Long id,
    String url,
    List<String> tags,
    List<String> filters

) {
}

