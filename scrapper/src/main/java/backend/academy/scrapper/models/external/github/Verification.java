package backend.academy.scrapper.models.external.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

record Verification(
    boolean verified,
    String reason,
    String signature,
    String payload,
    @JsonProperty("verified_at") OffsetDateTime verifiedAt
) {
}
