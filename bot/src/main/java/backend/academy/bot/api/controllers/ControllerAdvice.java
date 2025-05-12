package backend.academy.bot.api.controllers;

import backend.academy.bot.api.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ApiErrorResponse.class)
    public ResponseEntity<ApiErrorResponse> handle(ApiErrorResponse error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
