package backend.academy.scrapper.models.domain;

import backend.academy.scrapper.models.domain.ids.TagId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class Tag {
    private TagId tagId;
    private String value;

    public static Tag of(String value) {
        return builder()
            .tagId(new TagId(0L))
            .value(value)
            .build();
    }


}
