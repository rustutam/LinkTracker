package backend.academy.scrapper.models.domain;

import backend.academy.scrapper.models.domain.ids.LinkId;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Link {
    private LinkId linkId;
    private URI uri;
    private OffsetDateTime lastUpdateTime;
    private OffsetDateTime createdAt;

    public static Link of(URI uri) {
        return builder()
            .linkId(new LinkId(0L))
            .uri(uri)
            .lastUpdateTime(OffsetDateTime.MIN)
            .build();

    }
}
