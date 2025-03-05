package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.LinkInfo;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.val;
import org.springframework.stereotype.Repository;

@Repository
public class LinksRepositoryImpl implements LinksRepository {
    private Map<Long, List<LinkInfo>> userLinks = new HashMap<>();
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
    public LinkInfo saveLink(long chatId, LinkInfo linkInfo) {
        List<LinkInfo> allLinks = getAllLinks();
        Optional<LinkInfo> optionalLinkInfo = allLinks.stream()
            .filter(linkInfo1 -> linkInfo1.id() == linkInfo.id())
            .findFirst();

        if (optionalLinkInfo.isPresent()) {
            linkInfo.id(optionalLinkInfo.get().id());
        } else {
            long linkId = linksIdGenerator.getAndIncrement();
            linkInfo.id(linkId);
        }

        List<LinkInfo> links = userLinks.get(chatId);
        links.add(linkInfo);
        return linkInfo;
    }

    @Override
    public Optional<LinkInfo> deleteLink(long chatId, String url) {
        List<LinkInfo> linksInfo = userLinks.get(chatId);
        Optional<LinkInfo> optionalLinkToDelete = linksInfo.stream()
            .filter(linkInfo -> linkInfo.link().uri().toString().equals(url))
            .findFirst();

        optionalLinkToDelete.ifPresent(linksInfo::remove);

        return optionalLinkToDelete;
    }

    @Override
    public List<LinkInfo> findById(long chatId) {

        return userLinks.getOrDefault(chatId, List.of());
    }

    @Override
    public List<Long> getAllChatIds() {
        return userLinks.keySet().stream().toList();
    }

    @Override
    public List<LinkInfo> getAllLinks() {
        //возвращаем сслыки с уникальными id
        return userLinks.values().stream()
            .flatMap(List::stream)
            .collect(Collectors.collectingAndThen(
                Collectors.toMap(
                    LinkInfo::id,          // ключ - id
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
            .filter(entry -> entry.getValue().stream().anyMatch(link -> uri.equals(link.link().uri().toString())))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    @Override
    public List<LinkInfo> getGitHubLinks() {
        return getAllLinks().stream()
            .filter(linkInfo -> linkInfo.link().uri().getHost().equals("github.com"))
            .toList();
    }

    @Override
    public List<LinkInfo> getStackOverflowLinks() {
        return getAllLinks().stream()
            .filter(linkInfo -> linkInfo.link().uri().getHost().equals("stackoverflow.com"))
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
