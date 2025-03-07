package backend.academy.scrapper.service;

import backend.academy.scrapper.exceptions.AlreadyTrackLinkException;
import backend.academy.scrapper.exceptions.InvalidLinkException;
import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.exceptions.NotTrackLinkException;
import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.repository.api.GitHubExternalDataRepository;
import backend.academy.scrapper.repository.api.StackOverflowExternalDataRepository;
import backend.academy.scrapper.repository.database.LinksRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final LinksRepository linksRepository;
    private final List<String> correctDomains = List.of("github.com", "stackoverflow.com");

    @Override
    public Link addLink(long chatId, Link link) {
        if (!correctDomains.contains(link.uri().getHost())){
            throw new InvalidLinkException();
        }

        if (!linksRepository.isRegistered(chatId)) {
            throw new NotExistTgChatException();
        }

        List<String> links = linksRepository.findById(chatId)
            .stream()
            .map(Link::uri)
            .map(URI::toString)
            .toList();

        if (links.contains(link.uri().toString())) {
            throw new AlreadyTrackLinkException();
        }

        link.lastUpdateTime(OffsetDateTime.now());

        return linksRepository.saveLink(chatId, link);
    }

    @Override
    public Link removeLink(long chatId, String uri) {
        if (!linksRepository.isRegistered(chatId)) {
            throw new NotExistTgChatException();
        }

        return linksRepository.deleteLink(chatId, uri)
            .orElseThrow(NotTrackLinkException::new);
    }

    @Override
    public List<Link> getLinks(long chatId) {
        if (!linksRepository.isRegistered(chatId)) {
            throw new NotExistTgChatException();
        }
        return linksRepository.findById(chatId);
    }

}
