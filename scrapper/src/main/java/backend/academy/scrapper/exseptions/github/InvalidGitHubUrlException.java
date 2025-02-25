package backend.academy.scrapper.exseptions.github;

public class InvalidGitHubUrlException extends RuntimeException {
    public InvalidGitHubUrlException(String message) {
        super(message);
    }
}
