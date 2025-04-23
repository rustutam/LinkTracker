package backend.academy.scrapper.models.domain;

import backend.academy.scrapper.models.domain.ids.FilterId;
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

    public static Filter of(String value) {
        return builder()
                .filterId(new FilterId(0L))
                .value(value)
                .build();
    }
}
