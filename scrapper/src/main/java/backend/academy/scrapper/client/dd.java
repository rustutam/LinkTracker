//package backend.academy.scrapper.client;
//
//import backend.academy.scrapper.exseptions2.github.InvalidGitHubUrlException;
//import backend.academy.scrapper.exseptions2.github.NoCommitsException;
//import backend.academy.scrapper.models.external.github.CommitDto;
//import backend.academy.scrapper.models.external.github.RepositoryDto;
//import backend.academy.scrapper.proxies.GithubProxy;
//import java.net.URI;
//import java.time.OffsetDateTime;
//import java.util.Arrays;
//import java.util.Optional;
//import java.util.stream.Stream;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class ssdsdsdsGitHubExternalDataRepositoryImpl implements ExternalDataRepository {
//    private final GithubProxy githubProxy;
//
//    @Override
//    public OffsetDateTime getLastUpdateDate(String uri) {
//        URI repoUrl = URI.create(uri);
//        if (aisCorrectUri(repoUrl)) {
//            RepoInfo repoInfo = getRepoInfo(repoUrl);
//            RepositoryDto lastCommitRequest = githubProxy.repoRequest(repoInfo.owner, repoInfo.repo);
//            return Optional.ofNullable(lastCommitRequest)
//                .map(Arrays::stream)
//                .flatMap(Stream::findFirst)
//                .map(CommitDto::getCommitTime)
//                .orElseThrow(NoCommitsException::new);
//        } else {
//            throw new InvalidGitHubUrlException("Not a GitHub URL");
//        }
//    }
//
//    private boolean isCorrectUri(URI repoUrl) {
//        return repoUrl.getHost().equals("github.com");
//    }
//
//    private RepoInfo getRepoInfo(URI repoUrl) {
//        String[] parts = repoUrl.getPath().split("/");
//        return new RepoInfo(parts[0], parts[1]);
//    }
//
//    @Override
//    public OffsetDateTime getLastUpdateDate() {
//        return null;
//    }
//
//    private record RepoInfo(String owner, String repo) {
//    }
//}
