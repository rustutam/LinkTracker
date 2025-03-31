package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkMetadata;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import java.util.List;

public interface LinkMetadataRepository {
    List<LinkMetadata> findAllLinksByChatId(ChatId chatId);

}
