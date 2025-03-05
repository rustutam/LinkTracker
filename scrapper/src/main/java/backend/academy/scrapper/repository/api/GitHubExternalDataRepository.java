package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.client.GithubClient;
import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.models.external.github.RepositoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class GitHubExternalDataRepository implements ExternalDataRepository {
    private final GithubClient githubClient;

    @Override
    public Map<Long, OffsetDateTime> getLastUpdateDates(List<Link> linkList) {
        Map<Long, OffsetDateTime> updatedTimes = new HashMap<>();

        for (Link link : linkList) {
            RepoInfo repoInfo = getRepoInfo(link.uri());
            RepositoryDto repositoryDto = githubClient.repoRequest(repoInfo.owner, repoInfo.repo);
            OffsetDateTime updatedTime = repositoryDto.updatedAt();

            updatedTimes.put(link.id(),updatedTime);
        }

        return updatedTimes;
    }


    private RepoInfo getRepoInfo(URI repoUrl) {
        String[] parts = repoUrl.getPath().split("/");
        return new RepoInfo(parts[0], parts[1]);
    }

    private record RepoInfo(String owner, String repo) {
    }
}
