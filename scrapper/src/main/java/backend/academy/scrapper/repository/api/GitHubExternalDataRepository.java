package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.client.GithubClient;
import backend.academy.scrapper.models.LinkMetadata;
import backend.academy.scrapper.models.external.github.RepositoryDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GitHubExternalDataRepository implements ExternalDataRepository {
    private final GithubClient githubClient;

    @Override
    public List<LinkMetadata> getLinkLastUpdateDates(List<LinkMetadata> linkList) {
        return linkList.stream()
            .filter(linkMetadata -> isProcessingUri(linkMetadata.linkUri()))
            .map(linkMetadata ->
                new LinkMetadata(
                    linkMetadata.id(),
                    linkMetadata.linkUri(),
                    getLinkUpdatedDate(linkMetadata.linkUri())
                )
            )
            .toList();
    }

    private OffsetDateTime getLinkUpdatedDate(URI uri) {
        RepoInfo repoInfo = getRepoInfo(uri);
        RepositoryDto repositoryDto = githubClient.repoRequest(repoInfo.owner, repoInfo.repo);
        return repositoryDto.updatedAt();
    }

    @Override
    public boolean isProcessingUri(URI uri) {
        return uri.getHost().equals("github.com");
    }


    private RepoInfo getRepoInfo(URI repoUrl) {
        String[] parts = repoUrl.getPath().split("/");
        return new RepoInfo(parts[0], parts[1]);
    }

    private record RepoInfo(String owner, String repo) {
    }
}
