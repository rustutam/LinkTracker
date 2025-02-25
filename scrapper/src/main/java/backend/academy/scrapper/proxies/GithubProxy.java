package backend.academy.scrapper.proxies;

import backend.academy.scrapper.external.models.github.GitHubCommit;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GithubProxy {
    private final RestClient restClient;

    public GithubProxy() {
        this.restClient = RestClient.create();
    }

    public GitHubCommit[] getLastCommitRequest(String owner, String repository) {
        return restClient
            .get()
            .uri("repos/{owner}/{repo}/commits?per_page=1", owner, repository)
            .header("Authorization", "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")
            .retrieve()
            .body(GitHubCommit[].class);
    }

}
