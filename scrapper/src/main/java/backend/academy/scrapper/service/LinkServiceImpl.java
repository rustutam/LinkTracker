package backend.academy.scrapper.service;

import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.exceptions.NotTrackLinkException;
import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.models.LinkInfo;
import backend.academy.scrapper.repository.database.LinksRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final LinksRepository linksRepository;

    @Override
    public LinkInfo addLink(long chatId, Link link) {
        List<LinkInfo> linksById = linksRepository.findById(chatId);

        if (linksById.isEmpty()){
            throw new NotExistTgChatException();
        }

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.link(link);
        linkInfo.lastUpdateTime(OffsetDateTime.now());

        return linksRepository.saveLink(chatId, linkInfo);
    }

    @Override
    public LinkInfo removeLink(long chatId, String uri) {
        List<LinkInfo> linksById = linksRepository.findById(chatId);

        if (linksById.isEmpty()){
            throw new NotExistTgChatException();
        }

        return linksRepository.deleteLink(chatId, uri)
            .orElseThrow(NotTrackLinkException::new);
    }

    @Override
    public List<LinkInfo> getLinks(long chatId) {
        List<LinkInfo> linksById = linksRepository.findById(chatId);

        if (linksById.isEmpty()){
            throw new NotExistTgChatException();
        }
        return linksById;
    }

}
