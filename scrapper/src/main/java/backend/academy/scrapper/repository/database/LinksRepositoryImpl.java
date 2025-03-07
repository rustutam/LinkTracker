package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.models.LinkMetadata;
import backend.academy.scrapper.models.entities.InfoEntity;
import backend.academy.scrapper.models.entities.LinksEntity;
import backend.academy.scrapper.models.entities.RepositoryTables;
import backend.academy.scrapper.models.entities.UserLinksEntity;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class LinksRepositoryImpl implements LinksRepository {
    private final RepositoryTables repositoryTables;

    public LinksRepositoryImpl(RepositoryTables repositoryTables) {
        this.repositoryTables = repositoryTables;
    }

    private final AtomicLong userLinksIdGenerator = new AtomicLong(1);
    private final AtomicLong linksEntityIdGenerator = new AtomicLong(1);
    private final AtomicLong infoIdGenerator = new AtomicLong(1);

    private URI getUriById(long linkId) {
        return repositoryTables.linksEntities().stream()
            .filter(it -> it.linkId() == linkId)
            .map(LinksEntity::linkUri)
            .findFirst()
            .orElseThrow();
    }

    private OffsetDateTime getLastUpdateTimeById(long linkId) {
        return repositoryTables.linksEntities().stream()
            .filter(it -> it.linkId() == linkId)
            .map(LinksEntity::lastUpdateTime)
            .findFirst()
            .orElseThrow();
    }

    private List<String> getTagsById(long infoId) {
        return repositoryTables.infoEntities().stream()
            .filter(it -> it.infoId() == infoId)
            .map(InfoEntity::tags)
            .findFirst()
            .orElseThrow();
    }

    private List<String> getFiltersById(long infoId) {
        return repositoryTables.infoEntities().stream()
            .filter(ie -> ie.infoId() == infoId)
            .map(InfoEntity::filters)
            .findFirst()
            .orElseThrow();
    }

    private long getLinkIdByUri(URI linkUri) {
        return repositoryTables.linksEntities().stream()
            .filter(it -> it.linkUri().equals(linkUri))
            .map(it -> it.linkId())
            .findFirst()
            .orElseThrow();
    }

    private List<URI> getAllLinks() {
        return repositoryTables.linksEntities().stream()
            .map(it -> it.linkUri())
            .toList();
    }

    @Override
    public boolean isRegistered(long chatId) {
        return repositoryTables.users().contains(chatId);
    }

    @Override
    public void register(long chatId) {
        if (isRegistered(chatId)) {
            return; // Если пользователь уже зарегистрирован, ничего не делаем
        }
        repositoryTables.users().add(chatId);
    }

    @Override
    public void unRegister(long chatId) {
        if (!isRegistered(chatId)) {
            return; // Если пользователь не зарегистрирован, ничего не делаем
        }
        repositoryTables.users().remove(chatId);
        repositoryTables.userLinksEntities().removeIf(userLinksEntity -> userLinksEntity.chatId() == chatId);
    }

    @Override
    public Link saveLink(long chatId, Link link) {
// Проверяем, зарегистрирован ли пользователь
        long linkId = processLinkId(link);
        long infoId = processInfoId(link);

        // Добавляем связь пользователя с сохраненной ссылкой
        long generalId = userLinksIdGenerator.getAndIncrement();
        UserLinksEntity userLink = new UserLinksEntity(generalId, chatId, linkId, infoId);
        repositoryTables.userLinksEntities().add(userLink);

        // Возвращаем обновленный объект Link с присвоенным ID
        return new Link(generalId, link.uri(), link.tags(), link.filters(), link.lastUpdateTime());
    }

    private long processLinkId(Link link) {
        Optional<Long> maybeLinkId = repositoryTables.linksEntities().stream()
            .filter(it -> it.linkUri().equals(link.uri()))
            .map(LinksEntity::linkId)
            .findFirst();

        if (maybeLinkId.isEmpty()) {
            long linkId = linksEntityIdGenerator.getAndIncrement();
            repositoryTables.linksEntities().add(new LinksEntity(linkId, link.uri(), link.lastUpdateTime()));
            return linkId;
        } else {
            return maybeLinkId.get();
        }
    }

    private long processInfoId(Link link) {
        Optional<Long> maybeInfoId = repositoryTables.infoEntities().stream()
            .filter(it -> it.tags().stream().sorted().equals(link.tags().stream().sorted()))
            .filter(it -> it.filters().stream().sorted().equals(link.filters().stream().sorted()))
            .map(InfoEntity::infoId)
            .findFirst();

        if (maybeInfoId.isEmpty()) {
            long infoId = infoIdGenerator.getAndIncrement();
            repositoryTables.infoEntities().add(new InfoEntity(infoId, link.tags(), link.filters()));
            return infoId;
        } else {
            return maybeInfoId.get();
        }
    }

    @Override
    public Optional<Link> deleteLink(long chatId, String url) {
        Optional<UserLinksEntity> maybeDeletedLink = repositoryTables.userLinksEntities().stream()
            .filter(userLinksEntity -> userLinksEntity.chatId() == chatId)
            .filter(userLinksEntity -> userLinksEntity.linkId() == getLinkIdByUri(URI.create(url)))
            .findFirst();

        repositoryTables.userLinksEntities().removeIf(
            userLinksEntity ->
                userLinksEntity.chatId() == chatId && userLinksEntity.linkId() == getLinkIdByUri(URI.create(url))
        );

        if (maybeDeletedLink.isEmpty()) {
            return Optional.empty();
        }
        UserLinksEntity deletedLink = maybeDeletedLink.get();
        return Optional.of(
            new Link(
                deletedLink.id(),
                getUriById(deletedLink.linkId()),
                getTagsById(deletedLink.infoId()),
                getFiltersById(deletedLink.infoId()),
                getLastUpdateTimeById(deletedLink.linkId())
            )
        );

    }


    @Override
    public List<Link> findById(long chatId) {
        List<Link> links = new ArrayList<>();
        repositoryTables.userLinksEntities().stream()
            .filter(userLinksEntity -> userLinksEntity.chatId() == chatId)
            .forEach(userLinksEntity -> {
                Link link = new Link(
                    userLinksEntity.id(),
                    getUriById(userLinksEntity.linkId()),
                    getTagsById(userLinksEntity.infoId()),
                    getFiltersById(userLinksEntity.infoId()),
                    getLastUpdateTimeById(userLinksEntity.linkId())
                );
                links.add(link);
            });
        return links;
    }

    @Override
    public List<Long> getAllChatIdByLink(String uri) {
        long uriId = getLinkIdByUri(URI.create(uri));

        return repositoryTables.userLinksEntities().stream()
            .filter(userLinksEntity -> userLinksEntity.linkId() == uriId)
            .map(userLinksEntity -> userLinksEntity.chatId())
            .toList();
    }

    @Override
    public List<LinkMetadata> getGitHubLinks() {
        return getLinkMetadataByHost("github.com");
    }

    @Override
    public List<LinkMetadata> getStackOverflowLinks() {
        return getLinkMetadataByHost("stackoverflow.com");
    }

    private List<LinkMetadata> getLinkMetadataByHost(String host) {
        return repositoryTables.linksEntities().stream()
            .filter(it -> it.linkUri().getHost().equals(host))
            .map(it -> new LinkMetadata(it.linkId(), it.linkUri(), it.lastUpdateTime()))
            .toList();
    }

    @Override
    public void updateLinksLastUpdateTime(List<LinkMetadata> updatedLinks) {
        updatedLinks.forEach(linkWithNewDate -> {
            repositoryTables.linksEntities().stream()
                .filter(linkWithOldDate -> linkWithOldDate.linkId() == linkWithNewDate.id())
                .findFirst()
                .ifPresent(linkWithOldDate -> {
                    LinksEntity linksEntity = new LinksEntity(
                        linkWithOldDate.linkId(),
                        linkWithOldDate.linkUri(),
                        linkWithNewDate.lastUpdateTime()
                    );
                    repositoryTables.linksEntities().remove(linkWithOldDate);
                    repositoryTables.linksEntities().add(linksEntity);
                });
        });

    }

}
