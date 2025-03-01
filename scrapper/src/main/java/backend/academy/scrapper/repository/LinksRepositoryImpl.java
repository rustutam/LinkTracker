package backend.academy.scrapper.repository;

import backend.academy.scrapper.models.Link;
import lombok.val;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LinksRepositoryImpl implements LinksRepository {
    private Map<Long, List<Link>> userLinks = new HashMap<>();

    @Override
    public void register(long chatId) {
        if (userLinks.containsKey(chatId)) {
            //TODO выкидывать ошибку что пользователь уже существует
            return;
        }
        userLinks.put(chatId, List.of());

    }

    @Override
    public void unRegister(long chatId) {
        userLinks.remove(chatId);
    }

    @Override
    public Link saveLink(long chatId, Link link) {
        val links = userLinks.get(chatId);
        if (links == null) {
            //TODO выкидывать ошибку что пользователь не существует
            return null;
        }

        links.add(link);
        return link;
    }

    @Override
    public Optional<Link> deleteLink(long chatId, String url) {
        List<Link> links = userLinks.get(chatId);
        Optional<Link> linkToDelete = links.stream()
            .filter(link -> link.url().equals(url))
            .findFirst();

        linkToDelete.ifPresent(links::remove);

        return linkToDelete;
    }

    @Override
    public Optional<List<Link>> findAllLinksById(long chatId) {
        return Optional.ofNullable(userLinks.get(chatId));
    }

    @Override
    public List<Long> getAllChatIds(){
        return userLinks.keySet().stream().toList();
    }

    @Override
    public List<Link> getAllLinks() {
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
            .filter(entry ->  entry.getValue().stream().anyMatch(link -> uri.equals(link.url())))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }


}
