package backend.academy.responses;

public sealed interface BaseParseResponse permits GitHubParseResponse, StackOverflowParseResponse {}
