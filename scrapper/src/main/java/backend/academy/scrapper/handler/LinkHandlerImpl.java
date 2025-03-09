package backend.academy.scrapper.handler;

import backend.academy.GeneralParseLink;
import backend.academy.scrapper.exceptions.InvalidLinkException;
import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.service.LinkService;
import dto.request.AddLinkRequest;
import dto.request.RemoveLinkRequest;
import dto.response.LinkResponse;
import dto.response.ListLinksResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkHandlerImpl implements LinkHandler {
    private final LinkService linkService;

    @Override
    public LinkResponse addLink(long tgChatId, AddLinkRequest addLinkRequest) {
        if (new GeneralParseLink().start(addLinkRequest.link()) == null) {
            throw new InvalidLinkException();
        }

        Link link = new Link();
        link.uri(URI.create(addLinkRequest.link()));
        link.tags(addLinkRequest.tags());
        link.filters(addLinkRequest.filters());

        Link linkInfo = linkService.addLink(tgChatId, link);

        return new LinkResponse(linkInfo.id(), linkInfo.uri().toString(), linkInfo.tags(), linkInfo.filters());
    }

    @Override
    public LinkResponse removeLink(long tgChatId, RemoveLinkRequest removeLinkRequest) {
        Link link = linkService.removeLink(tgChatId, removeLinkRequest.link());

        return new LinkResponse(link.id(), link.uri().toString(), link.tags(), link.filters());
    }

    @Override
    public ListLinksResponse getLinks(long tgChatId) {
        List<Link> links = linkService.getLinks(tgChatId);

        List<LinkResponse> linkResponses = links.stream()
                .map(linkInfo ->
                        new LinkResponse(linkInfo.id(), linkInfo.uri().toString(), linkInfo.tags(), linkInfo.filters()))
                .toList();

        return new ListLinksResponse(linkResponses, linkResponses.size());
    }
}
