package backend.academy.bot.api.controllers;

import backend.academy.bot.api.dto.ApiErrorResponse;
import backend.academy.bot.exceptions.ApiScrapperErrorResponseException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ApiScrapperErrorResponseException.class)
    public ResponseEntity<ApiErrorResponse> handle(ApiErrorResponse error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ApiErrorResponse> handleRateLimitException(RequestNotPermitted ex) {
        var response = new ApiErrorResponse(
                "Too many requests.",
                Integer.toString(HttpStatus.TOO_MANY_REQUESTS.value()),
                ex.getClass().getName(),
                "Too many requests.",
                (ArrayList<String>) Arrays.stream(ex.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
    }
}
