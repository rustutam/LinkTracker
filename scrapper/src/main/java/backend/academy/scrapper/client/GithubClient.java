package backend.academy.scrapper.client;

import backend.academy.scrapper.models.external.github.RepositoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubClient {
    private final GithubRetryProxy proxy;

    public String issuesRequest(String owner, String repository) {
        return proxy.issuesRequest(owner, repository);
    }

    public RepositoryDto repoRequest(String owner, String repository) {
        return proxy.repoRequest(owner, repository);
    }
}
