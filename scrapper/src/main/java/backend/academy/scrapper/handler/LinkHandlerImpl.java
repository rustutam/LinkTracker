package backend.academy.scrapper.handler;

import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.models.LinkInfo;
import backend.academy.scrapper.models.api.request.AddLinkRequest;
import backend.academy.scrapper.models.api.request.RemoveLinkRequest;
import backend.academy.scrapper.models.api.response.LinkResponse;
import backend.academy.scrapper.models.api.response.ListLinksResponse;
import backend.academy.scrapper.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LinkHandlerImpl implements LinkHandler {
    private final LinkService linkService;

    @Override
    public LinkResponse addLink(long tgChatId, AddLinkRequest addLinkRequest) {
        //TODO подумать какую валидацию данных сделать
        Link link = new Link(
                addLinkRequest.link(),
                addLinkRequest.tags(),
                addLinkRequest.filters()
        );

        LinkInfo linkInfo = linkService.addLink(tgChatId, link);

        return new LinkResponse(
                linkInfo.id(),
                linkInfo.link().uri(),
                linkInfo.link().tags(),
                linkInfo.link().filters()
        );
    }

    @Override
    public LinkResponse removeLink(long tgChatId, RemoveLinkRequest removeLinkRequest) {
        LinkInfo linkInfo = linkService.removeLink(tgChatId, removeLinkRequest.link().toString());

        return new LinkResponse(
                linkInfo.id(),
                linkInfo.link().uri(),
                linkInfo.link().tags(),
                linkInfo.link().filters()
        );
    }

    @Override
    public ListLinksResponse getLinks(long tgChatId) {
        List<LinkInfo> links = linkService.getLinks(tgChatId);

        List<LinkResponse> linkResponses = links.stream()
                .map(linkInfo ->
                        new LinkResponse(
                                linkInfo.id(),
                                linkInfo.link().uri(),
                                linkInfo.link().tags(),
                                linkInfo.link().filters()
                        ))
                .toList();

        //TODO: убрать хардкод с size
        return new ListLinksResponse(
                linkResponses,
                5
        );
    }
}
