package backend.academy.scrapper.exseptions.github;

public class NoCommitsException extends RuntimeException {
    public NoCommitsException() {
        super("No commits found in repository");
    }
}
