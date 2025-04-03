package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.domain.LinkMetadata;
import backend.academy.scrapper.models.domain.ids.ChatId;
import java.util.List;

public interface LinkMetadataRepository {
    List<LinkMetadata> findAllLinkMetadataByChatId(ChatId chatId);

}
