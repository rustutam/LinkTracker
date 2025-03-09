package dto.response;

import jakarta.validation.Valid;

import java.util.List;

/**
 * ListLinksResponse
 */
public record ListLinksResponse(
    @Valid
    List<@Valid LinkResponse> links,
    Integer size
) {
}

