package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.client.GithubClient;
import backend.academy.scrapper.exceptions.InvalidLinkException;
import backend.academy.scrapper.models.LinkMetadata;
import backend.academy.scrapper.models.external.github.RepositoryDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GitHubExternalDataRepository implements ExternalDataRepository {
    private final GithubClient githubClient;

    @Override
    public List<LinkMetadata> getLinksWithNewLastUpdateDates(List<LinkMetadata> linkList) {
        return linkList.stream()
            .filter(linkMetadata -> isProcessingUri(linkMetadata.linkUri()))
            .map(linkMetadata ->
                new LinkMetadata(
                    linkMetadata.id(),
                    linkMetadata.linkUri(),
                    getLastUpdateDate(linkMetadata.linkUri())
                )
            )
            .toList();
    }

    @Override
    public OffsetDateTime getLastUpdateDate(URI uri) {
        try {
            RepoInfo repoInfo = getRepoInfo(uri);
            RepositoryDto repositoryDto = githubClient.repoRequest(repoInfo.owner, repoInfo.repo);
            return repositoryDto.updatedAt();
        } catch (Exception e) {
            throw new InvalidLinkException();
        }
    }

    @Override
    public boolean isProcessingUri(URI uri) {
        return uri.getHost().equals("github.com");
    }

    private RepoInfo getRepoInfo(URI repoUrl) {
        String[] parts = repoUrl.getPath().split("/");
        return new RepoInfo(parts[1], parts[2]);
    }

    private record RepoInfo(String owner, String repo) {
    }

}
