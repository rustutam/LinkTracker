package backend.academy.scrapper.repository.database.jpa.mapper;

import backend.academy.scrapper.models.domain.UpdatedLink;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.entity.UpdatedLinkEntity;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UpdatedLinkMapper {
    public UpdatedLink toDomain(UpdatedLinkEntity entity) {
        List<ChatId> chatIds = Arrays.stream(entity.chatIds().split(","))
                .map(s -> new ChatId(Long.parseLong(s))) // парсим в ChatId
                .toList();

        return new UpdatedLink(new LinkId(entity.linkId()), URI.create(entity.uri()), entity.description(), chatIds);
    }
}
