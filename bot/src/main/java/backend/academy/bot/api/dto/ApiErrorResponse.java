package backend.academy.bot.api.dto;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiErrorResponse extends RuntimeException {
    private final String description;
    private final String code;
    private final String exceptionName;
    private final String exceptionMessage;
    private final ArrayList<String> stacktrace;
}
