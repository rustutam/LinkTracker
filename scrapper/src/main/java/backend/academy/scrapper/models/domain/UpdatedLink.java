package backend.academy.scrapper.models.domain;

import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class UpdatedLink {
    private LinkId id;
    private URI uri;
    private String description;
    private List<ChatId> chatIds;
}
