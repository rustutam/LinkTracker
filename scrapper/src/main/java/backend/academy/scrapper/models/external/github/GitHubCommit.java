package backend.academy.scrapper.models.external.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public record GitHubCommit(
    String sha,
    @JsonProperty("node_id") String nodeId,
    Commit commit,
    URI url,
    @JsonProperty("html_url") URI htmlUrl,
    @JsonProperty("comments_url") URI commentsUrl,
    User author,
    User committer,
    List<Parent> parents
) {

    public OffsetDateTime getCommitTime() {
        return commit.author().date();
    }
}
