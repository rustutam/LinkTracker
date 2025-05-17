package backend.academy.scrapper.client;

import backend.academy.scrapper.configuration.clients.GitHubConfig;
import backend.academy.scrapper.exceptions.RepositoryNotFoundException;
import backend.academy.scrapper.models.external.github.RepositoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Slf4j
@Component
public class GithubClient {
    private final RestClient restClient;
    private final String token;

    public GithubClient(GitHubConfig gitHubConfig) {
        restClient = gitHubConfig.gitHubRestClient();
        token = gitHubConfig.token();
    }

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

    public RepositoryDto repoRequest(String owner, String repository) {
        RepositoryDto body = restClient
                .get()
                .uri("repos/{owner}/{repo}", owner, repository)
                .retrieve()
                .body(RepositoryDto.class);

        return body;
    }
}
