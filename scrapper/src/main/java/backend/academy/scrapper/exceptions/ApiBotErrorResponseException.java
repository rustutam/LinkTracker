package backend.academy.scrapper.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public class ApiBotErrorResponseException extends RuntimeException {
    private final String description;
    private final HttpStatusCode statusCode;
    private final String statusText;
    private final String exceptionMessage;
    private final StackTraceElement[] stacktrace;
}
