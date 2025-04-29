package backend.academy.scrapper.handler;

import backend.academy.GeneralParseLink;
import backend.academy.scrapper.exceptions.InvalidLinkException;
import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkMetadata;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.service.LinkService;
import dto.request.AddLinkRequest;
import dto.request.RemoveLinkRequest;
import dto.response.LinkResponse;
import dto.response.ListLinksResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javax.swing.text.html.HTMLDocument;

@Component
@RequiredArgsConstructor
public class LinkHandlerImpl implements LinkHandler {
    private final LinkService linkService;

    @Override
    public LinkResponse addLink(long chatId, AddLinkRequest addLinkRequest) {
        // TODO разобраться как тут лучше валидироваь сслыку чтобы не пропусктаь плохие ссылки внутрь
        if (new GeneralParseLink().start(addLinkRequest.link()) == null) {
            throw new InvalidLinkException();
        }
//
//        LinkMetadata mappedLinkMetadata = mapAddLinkRequestToLinkMetadata(addLinkRequest);

        LinkMetadata linkMetadata = linkService.addLink(
            new ChatId(chatId),
            URI.create(addLinkRequest.link()),
            addLinkRequest.tags(),
            addLinkRequest.filters()
            );

        return mapLinkMetadataToLinkResponse(linkMetadata);
    }

    @Override
    public LinkResponse removeLink(long chatId, RemoveLinkRequest removeLinkRequest) {

        LinkMetadata linkMetadata = linkService.removeLink(
            new ChatId(chatId),
            URI.create(removeLinkRequest.link())
        );

        return mapLinkMetadataToLinkResponse(linkMetadata);
    }

    @Override
    public ListLinksResponse getLinks(long chatId) {
        List<LinkMetadata> links = linkService.getLinks(new ChatId(chatId));

        List<LinkResponse> linkResponses = links.stream().map(this::mapLinkMetadataToLinkResponse).toList();

        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

//    private LinkMetadata mapAddLinkRequestToLinkMetadata(AddLinkRequest addLinkRequest){
//        Link link = Link.of(URI.create(addLinkRequest.link()));
//        List<Tag> tags = addLinkRequest.tags().stream().map(Tag::of).toList();
//        List<Filter> filters = addLinkRequest.filters().stream().map(Filter::of).toList();
//
//        return LinkMetadata.builder()
//            .link(link)
//            .tags(tags)
//            .filters(filters)
//            .build();
//    }

    private LinkResponse mapLinkMetadataToLinkResponse(LinkMetadata linkMetadata) {
        return new LinkResponse(
            linkMetadata.link().linkId().id(),
            linkMetadata.link().uri().toString(),
            linkMetadata.tags().stream().map(Tag::value).toList(),
            linkMetadata.filters().stream().map(Filter::value).toList()
        );
    }
}
