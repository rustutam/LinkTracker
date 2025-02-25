package backend.academy.scrapper.repository;


import backend.academy.scrapper.models.Link;
import java.util.List;
import java.util.Optional;


public interface LinksRepository {
    void register(long chatId);

    void unRegister(long chatId);

    Link saveLink(long chatId, Link link);

    Optional<Link> deleteLink(long chatId, String url);

    Optional<List<Link>> findAllLinksById(long chatId);

}
