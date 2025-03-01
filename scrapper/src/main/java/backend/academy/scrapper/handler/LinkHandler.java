package backend.academy.scrapper.handler;

import backend.academy.scrapper.models.api.request.AddLinkRequest;
import backend.academy.scrapper.models.api.request.RemoveLinkRequest;
import backend.academy.scrapper.models.api.response.LinkResponse;
import backend.academy.scrapper.models.api.response.ListLinksResponse;

public interface LinkHandler extends EndpointControleHandler{

    LinkResponse addLink(long tgChatId, AddLinkRequest addLinkRequest);
    LinkResponse removeLink(long tgChatId, RemoveLinkRequest addLinkRequest);
    ListLinksResponse getLinks(long tgChatId);
}
