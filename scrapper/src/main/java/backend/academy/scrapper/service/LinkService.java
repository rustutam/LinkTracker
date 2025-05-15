package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.LinkMetadata;
import backend.academy.scrapper.models.domain.ids.ChatId;
import java.net.URI;
import java.util.List;

public interface LinkService {

    LinkMetadata addLink(ChatId chatId, URI link, List<String> tags, List<String> filters);

    LinkMetadata removeLink(ChatId chatId, URI link);

    List<LinkMetadata> getLinks(ChatId chatId);
}
