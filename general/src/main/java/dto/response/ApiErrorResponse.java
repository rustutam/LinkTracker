package dto.response;

import jakarta.validation.Valid;
import lombok.Builder;

import java.util.List;

/**
 * ApiErrorResponse
 */
@Builder
public record ApiErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    @Valid
    List<String> stacktrace
) {
}

