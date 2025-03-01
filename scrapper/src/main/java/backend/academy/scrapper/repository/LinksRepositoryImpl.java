package backend.academy.scrapper.repository;

import backend.academy.scrapper.exceptions.NotExistLinkException;
import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.models.LinkInfo;
import lombok.val;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class LinksRepositoryImpl implements LinksRepository {
    private Map<Long, List<LinkInfo>> userLinks = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public void register(long chatId) {
        userLinks.put(chatId, List.of());

    }

    @Override
    public void unRegister(long chatId) {
        userLinks.remove(chatId);
    }

    @Override
    public LinkInfo saveLink(long chatId, LinkInfo linkInfo) {
        long id = idGenerator.getAndIncrement();
        linkInfo.id(id);

        val links = userLinks.get(chatId);
        links.add(linkInfo);
        return linkInfo;
    }

    @Override
    public Optional<LinkInfo> deleteLink(long chatId, String url) {
        List<LinkInfo> linksInfo = userLinks.get(chatId);
        Optional<LinkInfo> optionalLinkToDelete = linksInfo.stream()
            .filter(linkInfo -> linkInfo.link().url().toString().equals(url))
            .findFirst();

        optionalLinkToDelete.ifPresent(linksInfo::remove);

        return optionalLinkToDelete;
    }

    @Override
    public Optional<List<LinkInfo>> findById(long chatId) {
        return Optional.ofNullable(userLinks.get(chatId));
    }

    @Override
    public List<Long> getAllChatIds(){
        return userLinks.keySet().stream().toList();
    }

    @Override
    public List<LinkInfo> getAllLinks() {
        return userLinks
            .values()
            .stream()
            .flatMap(List::stream)
            .distinct()
            .collect(Collectors.toList());
    }

    @Override
    public List<Long> getAllChatIdByLink(String uri) {
        return userLinks
            .entrySet()
            .stream()
            .filter(entry ->  entry.getValue().stream().anyMatch(link -> uri.equals(link.link().url().toString())))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }


}
