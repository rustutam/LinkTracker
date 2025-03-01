package backend.academy.scrapper.exseptions2.github;

public class NoCommitsException extends RuntimeException {
    public NoCommitsException() {
        super("No commits found in repository");
    }
}
