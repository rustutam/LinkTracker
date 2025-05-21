package backend.academy.bot.api.dto;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public class ApiErrorResponse extends RuntimeException {
    private final String description;
    private final HttpStatusCode statusCode;
    private final String statusText;
    private final String exceptionMessage;
    private final StackTraceElement[] stacktrace;
}
