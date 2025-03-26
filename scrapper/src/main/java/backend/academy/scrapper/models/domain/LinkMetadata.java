package backend.academy.scrapper.models.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LinkMetadata {
    private Link link;
    private List<Tag> tags;
    private List<Filter> filters;
}
