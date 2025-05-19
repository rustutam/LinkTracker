package backend.academy.scrapper.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@AllArgsConstructor
public class ApiGitHubErrorResponseException extends RuntimeException {
    private final String description;
    private final HttpStatusCode statusCode;
    private final String statusText;
    private final String exceptionMessage;
    private final StackTraceElement[] stacktrace;
}
