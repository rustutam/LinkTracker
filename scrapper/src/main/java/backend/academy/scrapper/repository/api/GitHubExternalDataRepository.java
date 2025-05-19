package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.client.GithubClient;
import backend.academy.scrapper.exceptions.ApiGitHubErrorResponseException;
import backend.academy.scrapper.models.domain.ChangeInfo;
import backend.academy.scrapper.models.domain.Link;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
@SuppressWarnings("StringSplitter")
public class GitHubExternalDataRepository extends ExternalDataRepository {
    private static final String PR_ISSUE_DESCRIPTION = "Новый PR/Issue";

    private final GithubClient githubClient;
    private final ObjectMapper objectMapper;

    @Override
    public List<ChangeInfo> getChangeInfoByLink(Link link) {
        RepoInfo repoInfo = getRepoInfo(link.uri());
        return githubClient.issuesRequest(repoInfo.owner, repoInfo.repo)
            .map(response -> parseIssues(link, response))
            .orElse(List.of());
    }

    private List<ChangeInfo> parseIssues(Link link, String response) {
        List<ChangeInfo> allContent = new ArrayList<>();
        try {
            JsonNode jsonResponse = objectMapper.readTree(response);

            jsonResponse.forEach(content -> {
                ChangeInfo changeInfo = ChangeInfo.builder()
                        .description(PR_ISSUE_DESCRIPTION)
                        .title(content.get("title").asText())
                        .username(content.get("user").get("login").asText())
                        .creationTime(
                                OffsetDateTime.parse(content.get("created_at").asText()))
                        .preview(truncatePreview(content.get("body").asText()))
                        .build();

                allContent.add(changeInfo);
            });
            return allContent;
        } catch (JsonProcessingException e) {
            log.atError()
                    .addKeyValue("link", link.uri().toString())
                    .setMessage("Ошибка при обработке github контента")
                    .setCause(e)
                    .log();
            return List.of();
        }
    }
    //    Для GitHub новый PR или Issue, сообщение включает:
    //    название PR или Issue
    //    имя пользователя
    //    время создания
    //    превью описания (первые 200 символов)

    @Override
    protected boolean isProcessingUri(URI uri) {
        return uri.getHost().equals("github.com");
    }

    private RepoInfo getRepoInfo(URI repoUrl) {
        String[] parts = repoUrl.getPath().split("/");
        return new RepoInfo(parts[1], parts[2]);
    }

    private record RepoInfo(String owner, String repo) {}
}
