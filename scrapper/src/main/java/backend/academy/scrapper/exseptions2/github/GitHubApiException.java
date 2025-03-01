package backend.academy.scrapper.exseptions2.github;

import org.springframework.http.HttpStatusCode;

public class GitHubApiException extends RuntimeException {
    private final HttpStatusCode statusCode;

    public GitHubApiException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
