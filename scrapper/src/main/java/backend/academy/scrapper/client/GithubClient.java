package backend.academy.scrapper.client;

import backend.academy.scrapper.exceptions.ApiGitHubErrorResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubClient {
    private final GithubRetryProxy proxy;

    public Optional<String> issuesRequest(String owner, String repository) {
        try {
            return Optional.of(proxy.issuesRequest(owner, repository));
        } catch (ApiGitHubErrorResponseException e) {
            return Optional.empty();
        }
    }

    public Optional<String> repoRequest(String owner, String repository) {
        try {
            return Optional.of(proxy.repoRequest(owner, repository));
        } catch (ApiGitHubErrorResponseException e) {
            return Optional.empty();
        }
    }
}
