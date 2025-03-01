package backend.academy.scrapper.models.external.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

record Commit(
    Author author,
    Author committer,
    String message,
    Tree tree,
    URI url,
    @JsonProperty("comment_count") int commentCount,
    Verification verification
) {
}
