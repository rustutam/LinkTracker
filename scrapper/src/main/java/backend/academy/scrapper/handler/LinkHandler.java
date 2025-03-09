package backend.academy.scrapper.handler;


import dto.request.AddLinkRequest;
import dto.request.RemoveLinkRequest;
import dto.response.LinkResponse;
import dto.response.ListLinksResponse;

public interface LinkHandler extends EndpointControleHandler{

    LinkResponse addLink(long tgChatId, AddLinkRequest addLinkRequest);
    LinkResponse removeLink(long tgChatId, RemoveLinkRequest addLinkRequest);
    ListLinksResponse getLinks(long tgChatId);
}
