package backend.academy.scrapper.client;

import backend.academy.scrapper.configuration.GitHubConfig;
import backend.academy.scrapper.exceptions.RepositoryNotFoundException;
import backend.academy.scrapper.models.external.github.CommitDto;
import backend.academy.scrapper.models.external.github.IssueDto;
import backend.academy.scrapper.models.external.github.RepositoryDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Slf4j
@Component
public class GithubClient {
    private final RestClient restClient;
    private final GitHubConfig gitHubConfig;

    public GithubClient(GitHubConfig gitHubConfig, ObjectMapper objectMapper, GitHubConfig gitHubConfig1) {
        restClient = RestClient.builder()
            .baseUrl(gitHubConfig.baseUrl())
            .build();
        this.gitHubConfig = gitHubConfig1;
    }

    public String issuesRequest(String owner, String repository) {
        try {
            return restClient
                .get()
                .uri("repos/{owner}/{repo}/issues", owner, repository)
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + gitHubConfig.githubToken())
                .retrieve()
                .body(String.class);
        } catch (RestClientResponseException e ) {
            log.atError()
                .setMessage("Не удалось найти репозиторий")
                .log();
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
