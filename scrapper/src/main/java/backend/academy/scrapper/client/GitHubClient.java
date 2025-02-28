package backend.academy.scrapper.client;

import backend.academy.scrapper.exseptions.github.InvalidGitHubUrlException;
import backend.academy.scrapper.exseptions.github.NoCommitsException;
import backend.academy.scrapper.external.models.github.GitHubCommit;
import backend.academy.scrapper.proxies.GithubProxy;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubClient implements Client{
    private final GithubProxy githubProxy;

    @Override
    public OffsetDateTime getLastUpdateDate(String uri) {
        URI repoUrl = URI.create(uri);
        if (isCorrectUri(repoUrl)) {
            RepoInfo repoInfo = getRepoInfo(repoUrl);
            GitHubCommit[] lastCommitRequest = githubProxy.getLastCommitRequest(repoInfo.owner, repoInfo.repo);
            return Optional.ofNullable(lastCommitRequest)
                .map(Arrays::stream)
                .flatMap(Stream::findFirst)
                .map(GitHubCommit::getCommitTime)
                .orElseThrow(NoCommitsException::new);
        } else {
            throw new InvalidGitHubUrlException("Not a GitHub URL");
        }
    }

    private boolean isCorrectUri(URI repoUrl) {
        return repoUrl.getHost().equals("github.com");
    }

    private RepoInfo getRepoInfo(URI repoUrl) {
        String[] parts = repoUrl.getPath().split("/");
        return new RepoInfo(parts[0], parts[1]);
    }

    private record RepoInfo(String owner, String repo) {
    }
}
