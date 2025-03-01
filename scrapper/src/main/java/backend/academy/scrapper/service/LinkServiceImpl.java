package backend.academy.scrapper.service;

import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.repository.LinksRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final LinksRepository linksRepository;

    @Override
    public Link addLink(long chatId, String link, List<String> tags, List<String> filters) {
        // TODO проверять что если чата нет то мы не сможем добавить
        long randomLong = ThreadLocalRandom.current().nextLong();
        Link linkModel = new Link(randomLong, link, tags, filters);
        return linksRepository.saveLink(chatId, linkModel);
    }

    @Override
    public Link removeLink(long chatId, String uri) {
        Optional<Link> link = linksRepository.deleteLink(chatId, uri);

        if (link.isEmpty()) {
            return null;
        }
        return link.get();
    }

    @Override
    public List<Link> getListLinks(long chatId) {
        Optional<List<Link>> allLinksById = linksRepository.findAllLinksById(chatId);
        if (allLinksById.isEmpty()) {
            //TODO выкидываем ошибку что у чата нет активных ссылок
            return null;
        }
        return allLinksById.get();
    }

}
