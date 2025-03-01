package backend.academy.scrapper.exseptions2.github;

public class InvalidGitHubUrlException extends RuntimeException {
    public InvalidGitHubUrlException(String message) {
        super(message);
    }
}
