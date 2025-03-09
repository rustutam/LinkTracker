package backend.academy.scrapper.models.external.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommitDto(
        String sha,
        @JsonProperty("node_id") String nodeId,
        CommitDetails commit,
        String url,
        @JsonProperty("html_url") String htmlUrl,
        @JsonProperty("comments_url") String commentsUrl,
        UserDto author,
        UserDto committer,
        List<ParentCommit> parents) {

    public OffsetDateTime getCommitTime() {
        return commit.author().date();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CommitDetails(
            GitUser author,
            GitUser committer,
            String message,
            Tree tree,
            String url,
            @JsonProperty("comment_count") Integer commentCount,
            Verification verification) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GitUser(String name, String email, OffsetDateTime date) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Tree(String sha, String url) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Verification(
            Boolean verified,
            String reason,
            String signature,
            String payload,
            @JsonProperty("verified_at") OffsetDateTime verifiedAt) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record UserDto(
            String login,
            Long id,
            @JsonProperty("node_id") String nodeId,
            @JsonProperty("avatar_url") String avatarUrl,
            @JsonProperty("html_url") String htmlUrl,
            String type) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ParentCommit(String sha, String url, @JsonProperty("html_url") String htmlUrl) {}
}
