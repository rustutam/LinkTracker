package backend.academy.scrapper.client;

import backend.academy.scrapper.configuration.GitHubConfig;
import backend.academy.scrapper.models.external.github.CommitDto;
import backend.academy.scrapper.models.external.github.IssueDto;
import backend.academy.scrapper.models.external.github.RepositoryDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GithubClient {
    private final RestClient restClient;

    public GithubClient(GitHubConfig gitHubConfig) {
        restClient = RestClient.builder().baseUrl(gitHubConfig.baseUrl()).build();
    }

    public CommitDto[] commitsRequest(String owner, String repository) {
        return restClient
                .get()
                .uri("repos/{owner}/{repo}/commits?per_page=1", owner, repository)
                .retrieve()
                .body(CommitDto[].class);
    }

    public IssueDto[] issuesRequest(String owner, String repository) {
        return restClient
                .get()
                .uri("repos/{owner}/{repo}/issues?per_page=1", owner, repository)
                .retrieve()
                .body(IssueDto[].class);
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
