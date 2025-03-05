package backend.academy.scrapper.repository.database;


import backend.academy.scrapper.models.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


public interface LinksRepository {
    void register(long chatId);

    void unRegister(long chatId);

    boolean isRegistered(long chatId);

    Link saveLink(long chatId, Link link);

    Optional<Link> deleteLink(long chatId, String url);

    List<Link> findById(long chatId);

    List<Long> getAllChatIds();

    List<Link> getAllLinks();

    List<Long> getAllChatIdByLink(String uri);

    List<Link> getGitHubLinks();

    List<Link> getStackOverflowLinks();

    void updateLastUpdateTime(long id, OffsetDateTime time);
}
