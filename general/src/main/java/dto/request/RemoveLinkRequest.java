package dto.request;

import jakarta.validation.constraints.NotNull;

import java.net.URI;

/**
 * RemoveLinkRequest
 */
public record RemoveLinkRequest(
    @NotNull String link
) {
}

