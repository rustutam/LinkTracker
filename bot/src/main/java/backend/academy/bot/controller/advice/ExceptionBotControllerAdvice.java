package backend.academy.bot.controller.advice;

import dto.response.ApiErrorResponse;
import general.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionBotControllerAdvice {
    private final ExceptionUtils exceptionUtils;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalid(Exception ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                "Некорректные параметры запроса",
                "400",
                ex.getClass().getName(),
                ex.getMessage(),
                exceptionUtils.getStacktrace(ex));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
