package backend.academy.scrapper.repository.database;


import backend.academy.scrapper.models.LinkInfo;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


public interface LinksRepository {
    void register(long chatId);

    void unRegister(long chatId);

    LinkInfo saveLink(long chatId, LinkInfo link);

    Optional<LinkInfo> deleteLink(long chatId, String url);

    List<LinkInfo> findById(long chatId);

    List<Long> getAllChatIds();

    List<LinkInfo> getAllLinks();

    List<Long> getAllChatIdByLink(String uri);

    List<LinkInfo> getGitHubLinks();

    List<LinkInfo> getStackOverflowLinks();

    void updateLastUpdateTime(long id, OffsetDateTime time);
}
