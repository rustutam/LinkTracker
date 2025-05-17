package backend.academy.scrapper.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiGitHubErrorResponse extends RuntimeException {
    private String message;
    private String errors;
    private String documentationUrl;
    private String status;
}
