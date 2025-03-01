package backend.academy.scrapper.models.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * AddLinkRequest
 */

@RequiredArgsConstructor
@EqualsAndHashCode
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-02-27T07:36:33.430797072Z[UTC]")
public class AddLinkRequest {

    private final URI link;

    @Valid
    @Schema(name = "link", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("link")
    public URI getLink() {
        return link;
    }

}

