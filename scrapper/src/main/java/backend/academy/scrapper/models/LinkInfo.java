package backend.academy.scrapper.models;

import java.time.OffsetDateTime;
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
public class LinkInfo {
    private long id;
    private Link link;
    private OffsetDateTime lastUpdateTime;
}
