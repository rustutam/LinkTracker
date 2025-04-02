package backend.academy.scrapper.models.external.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IssueDto(
    String url,
    @JsonProperty("repository_url") String repositoryUrl,
    @JsonProperty("html_url") String htmlUrl,
    Long id,
    @JsonProperty("node_id") String nodeId,
    Integer number,
    String title,
    UserDto user,
    List<Object> labels, // Можно заменить на конкретный DTO для labels
    String state,
    Boolean locked,
    Integer comments,
    @JsonProperty("created_at") LocalDateTime createdAt,
    @JsonProperty("updated_at") LocalDateTime updatedAt,
    @JsonProperty("closed_at") LocalDateTime closedAt,
    @JsonProperty("author_association") String authorAssociation,
    String body,
    ReactionsDto reactions
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record UserDto(
        String login,
        Long id,
        @JsonProperty("avatar_url") String avatarUrl,
        @JsonProperty("html_url") String htmlUrl,
        String type) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ReactionsDto(
        @JsonProperty("total_count") Integer totalCount,
        @JsonProperty("+1") Integer plusOne,
        @JsonProperty("-1") Integer minusOne,
        Integer laugh,
        Integer hooray,
        Integer confused,
        Integer heart,
        Integer rocket,
        Integer eyes) {
    }
}
