package backend.academy.scrapper.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiStackOverflowErrorResponse extends RuntimeException {
    private Integer errorId;
    private String errorMessage;
    private String errorName;
}
