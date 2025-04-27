package backend.academy.scrapper.models.domain;

import backend.academy.scrapper.models.domain.ids.FilterId;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Filter {
    private FilterId filterId;
    private String value;
    private OffsetDateTime createdAt;
}
