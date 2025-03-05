package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.Link;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class LinksRepositoryImpl implements LinksRepository {
    private Map<Long, List<Link>> userLinks = new HashMap<>();
    private final AtomicLong linksIdGenerator = new AtomicLong(1);

    @Override
    public void register(long chatId) {
        userLinks.put(chatId, new ArrayList<>());

    }

    @Override
    public void unRegister(long chatId) {
        userLinks.remove(chatId);
    }

    @Override
    public boolean isRegistered(long chatId) {
        List<Long> allChatIds = userLinks.keySet().stream().toList();
        return allChatIds.contains(chatId);
    }

    @Override
    public Link saveLink(long chatId, Link link) {
        List<Link> allLinks = getAllLinks();
        Optional<Link> optionalLinkInfo = allLinks.stream()
            .filter(linkInfo1 -> linkInfo1.id() == link.id())
            .findFirst();

        if (optionalLinkInfo.isPresent()) {
            link.id(optionalLinkInfo.get().id());
        } else {
            long linkId = linksIdGenerator.getAndIncrement();
            link.id(linkId);
        }

        List<Link> links = userLinks.get(chatId);
        links.add(link);
        return link;
    }

    @Override
    public Optional<Link> deleteLink(long chatId, String url) {
        List<Link> linksInfo = userLinks.get(chatId);
        Optional<Link> optionalLinkToDelete = linksInfo.stream()
            .filter(linkInfo -> linkInfo.uri().toString().equals(url))
            .findFirst();

        optionalLinkToDelete.ifPresent(linksInfo::remove);

        return optionalLinkToDelete;
    }

    @Override
    public List<Link> findById(long chatId) {

        return userLinks.getOrDefault(chatId, List.of());
    }

    @Override
    public List<Long> getAllChatIds() {
        return userLinks.keySet().stream().toList();
    }

    @Override
    public List<Link> getAllLinks() {
        //возвращаем сслыки с уникальными id
        return userLinks.values().stream()
            .flatMap(List::stream)
            .collect(Collectors.collectingAndThen(
                Collectors.toMap(
                    Link::id,          // ключ - id
                    Function.identity(),  // значение - сам объект
                    (existing, replacement) -> existing // при конфликте оставляем существующий
                ),
                map -> new ArrayList<>(map.values())
            ));
    }

    @Override
    public List<Long> getAllChatIdByLink(String uri) {
        return userLinks
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue().stream().anyMatch(link -> uri.equals(link.uri().toString())))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    @Override
    public List<Link> getGitHubLinks() {
        return getAllLinks().stream()
            .filter(linkInfo -> linkInfo.uri().getHost().equals("github.com"))
            .toList();
    }

    @Override
    public List<Link> getStackOverflowLinks() {
        return getAllLinks().stream()
            .filter(linkInfo -> linkInfo.uri().getHost().equals("stackoverflow.com"))
            .toList();
    }

    @Override
    public void updateLastUpdateTime(long id, OffsetDateTime time) {
        userLinks.values()
            .stream()
            .flatMap(List::stream)
            .filter(linkInfo -> id == linkInfo.id())
            .forEach(linkInfo -> linkInfo.lastUpdateTime(time));
    }


}
