package backend.academy.responses;

public record GitHubParseResponse(String user, String repo) implements BaseParseResponse {}
