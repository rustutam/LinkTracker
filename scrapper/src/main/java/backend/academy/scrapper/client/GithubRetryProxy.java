package backend.academy.scrapper.client;

import backend.academy.scrapper.configuration.clients.GitHubConfig;
import backend.academy.scrapper.exceptions.ApiGitHubErrorResponseException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class GithubRetryProxy {
    public static final String GITHUB_API_CLIENT_ERROR_RESPONSE_MESSAGE = "Ошибка получения ответа от API GitHub";
    private final RestClient restClient;
    private final String token;
    private final int pageSize;

    public GithubRetryProxy(GitHubConfig gitHubConfig) {
        restClient = gitHubConfig.gitHubRestClient();
        token = gitHubConfig.token();
        pageSize = gitHubConfig.pageSize();
    }

    @Retry(name = "github")
    @CircuitBreaker(name = "cb1")
    public String issuesRequest(String owner, String repository) throws ApiGitHubErrorResponseException {
        try {
            return restClient
                .get()
                .uri("repos/{owner}/{repo}/issues?per_page={}", owner, repository, pageSize)
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class);

        } catch (HttpClientErrorException e) {
            logHttpClientError(e, owner, repository);

            throw new ApiGitHubErrorResponseException(
                GITHUB_API_CLIENT_ERROR_RESPONSE_MESSAGE,
                e.getStatusCode(),
                e.getStatusText(),
                e.getMessage(),
                e.getStackTrace()
            );
        }
    }

    @Retry(name = "github")
    @CircuitBreaker(name = "cb1")
    public String repoRequest(String owner, String repository) {
        try {
            return restClient
                .get()
                .uri("repos/{owner}/{repo}", owner, repository)
                .retrieve()
                .body(String.class);
        } catch (HttpClientErrorException e) {

            logHttpClientError(e, owner, repository);

            throw new ApiGitHubErrorResponseException(
                GITHUB_API_CLIENT_ERROR_RESPONSE_MESSAGE,
                e.getStatusCode(),
                e.getStatusText(),
                e.getMessage(),
                e.getStackTrace()
            );
        }
    }

    private void logHttpClientError(HttpClientErrorException e, String owner, String repository) {
        log.atError()
            .setMessage(GITHUB_API_CLIENT_ERROR_RESPONSE_MESSAGE)
            .addKeyValue("owner", owner)
            .addKeyValue("repository name", repository)
            .addKeyValue("statusCode", e.getStatusCode().value())
            .addKeyValue("statusText", e.getStatusText())
            .log();
    }
}
