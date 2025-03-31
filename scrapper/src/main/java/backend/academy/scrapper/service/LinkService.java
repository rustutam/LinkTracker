package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.LinkMetadata;
import backend.academy.scrapper.models.domain.ids.ChatId;
import java.net.URI;
import java.util.List;

public interface LinkService extends EndpointControleService {

    LinkMetadata addLink(ChatId chatId, LinkMetadata link);

    LinkMetadata removeLink(ChatId chatId, URI link);

    List<LinkMetadata> getLinks(ChatId chatId);
}
