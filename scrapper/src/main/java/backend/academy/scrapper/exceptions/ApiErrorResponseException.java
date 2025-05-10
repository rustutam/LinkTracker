package backend.academy.scrapper.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiErrorResponseException extends RuntimeException {
    private final String description;
    private final String code;
    private final String exceptionName;
    private final String exceptionMessage;
    private final String[] stacktrace;
}
