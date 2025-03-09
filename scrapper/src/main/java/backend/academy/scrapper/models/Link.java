package backend.academy.scrapper.models;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Link {
    private long id;
    private URI uri;
    private List<String> tags;
    private List<String> filters;
    private OffsetDateTime lastUpdateTime;
}
