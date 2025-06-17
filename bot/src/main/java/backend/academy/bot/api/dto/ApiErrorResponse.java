package backend.academy.bot.api.dto;

import jakarta.validation.Valid;
import java.util.List;

public record ApiErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    @Valid List<String> stacktrace) {
}
