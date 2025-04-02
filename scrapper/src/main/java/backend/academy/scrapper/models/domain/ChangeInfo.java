package backend.academy.scrapper.models.domain;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ChangeInfo {
    private String title;
    private String username;
    private OffsetDateTime creationTime;
    private String preview;
}
