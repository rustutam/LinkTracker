package backend.academy.scrapper.models.domain;

import backend.academy.scrapper.models.domain.ids.TagId;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Tag {
    private TagId tagId;
    private String value;
    private OffsetDateTime createdAt;
}
