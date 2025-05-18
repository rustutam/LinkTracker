package backend.academy.scrapper.client;

import backend.academy.scrapper.configuration.clients.GitHubConfig;
import backend.academy.scrapper.exceptions.RepositoryNotFoundException;
import backend.academy.scrapper.models.external.github.RepositoryDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Slf4j
@Service
public class GithubRetryProxy {
    private final RestClient restClient;
    private final String token;

    public GithubRetryProxy(GitHubConfig gitHubConfig) {
        restClient = gitHubConfig.gitHubRestClient();
        token = gitHubConfig.token();
    }

    @Retry(name = "github")
    @CircuitBreaker(name = "cb1")
    public String issuesRequest(String owner, String repository) {
        try {
            return restClient
                    .get()
                    .uri("repos/{owner}/{repo}/issues", owner, repository)
                    .header("Accept", "application/vnd.github+json")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            log.atError().setMessage("Не удалось найти репозиторий").log();
            throw new RepositoryNotFoundException("Репозиторий не найден");
        }
    }

    @Retry(name = "github")
    @CircuitBreaker(name = "cb1")
    public RepositoryDto repoRequest(String owner, String repository) {
        return restClient
                .get()
                .uri("repos/{owner}/{repo}", owner, repository)
                .retrieve()
                .body(RepositoryDto.class);
    }
}
