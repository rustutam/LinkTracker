package dto.response;

import jakarta.validation.Valid;
import java.util.List;
import lombok.Builder;

/** ApiErrorResponse */
@Builder
public record ApiErrorResponse(
        String description,
        String code,
        String exceptionName,
        String exceptionMessage,
        @Valid List<String> stacktrace) {}
