package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.client.GithubClient;
import backend.academy.scrapper.models.domain.ChangeInfo;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkChangeStatus;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GitHubExternalDataRepository extends ExternalDataRepository {
    private final GithubClient githubClient;
    private final ObjectMapper objectMapper;

    @Override
    public List<ChangeInfo> getChangeInfoByLink(Link link) {
        //TODO написано на скорую руку, переделать
        List<ChangeInfo> allContent = new ArrayList<>();
        RepoInfo repoInfo = getRepoInfo(link.uri());
        String responce = githubClient.issuesRequest(repoInfo.owner, repoInfo.repo);

        try {
            JsonNode jsonResponse = objectMapper.readTree(responce);

            jsonResponse.forEach(content -> {
                    ChangeInfo changeInfo = ChangeInfo.builder()
                        .title(content.get("title").asText())
                        .username(content.get("user").get("login").asText())
                        .creationTime(OffsetDateTime.parse(content.get("created_at").asText()))
                        .preview(content.get("body").asText())
                        .build();

                    allContent.add(changeInfo);
                }
            );
            return allContent;
        } catch (HttpMessageNotReadableException | JsonProcessingException e) {
            log.atError()
                .addKeyValue("link", link.uri().toString())
                .setMessage("Ошибка при получении github контента")
                .log();
            throw new HttpMessageNotReadableException("Не удаётся прочитать поле 'updated_at'");
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

    private record RepoInfo(String owner, String repo) {
    }
}
