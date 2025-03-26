package backend.academy.scrapper.handler;

import dto.request.AddLinkRequest;
import dto.request.RemoveLinkRequest;
import dto.response.LinkResponse;
import dto.response.ListLinksResponse;

public interface LinkHandler extends EndpointControleHandler {

    LinkResponse addLink(long chatId, AddLinkRequest addLinkRequest);

    LinkResponse removeLink(long chatId, RemoveLinkRequest addLinkRequest);

    ListLinksResponse getLinks(long chatId);
}
