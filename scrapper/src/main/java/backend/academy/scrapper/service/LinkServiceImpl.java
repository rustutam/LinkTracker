package backend.academy.scrapper.service;

import backend.academy.scrapper.exceptions.NotExistLinkException;
import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.exceptions.NotTrackLinkException;
import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.models.LinkInfo;
import backend.academy.scrapper.repository.LinksRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final LinksRepository linksRepository;

    @Override
    public LinkInfo addLink(long chatId, Link link) {
        linksRepository.findById(chatId)
            .orElseThrow(NotExistTgChatException::new);

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.link(link);
        linkInfo.lastUpdateTime(OffsetDateTime.now());

        return linksRepository.saveLink(chatId, linkInfo);
    }

    @Override
    public LinkInfo removeLink(long chatId, String uri) {
        linksRepository.findById(chatId)
            .orElseThrow(NotExistTgChatException::new);

        return linksRepository.deleteLink(chatId, uri)
            .orElseThrow(NotTrackLinkException::new);
    }

    @Override
    public List<LinkInfo> getLinks(long chatId) {
        return linksRepository.findById(chatId)
            .orElseThrow(NotExistTgChatException::new);
    }

}
