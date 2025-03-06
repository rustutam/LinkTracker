package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.models.LinkMetadata;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class LinksRepositoryImpl implements LinksRepository {
    private List<Long> users = new ArrayList<>();

    private List<UserLinksEntity> userLinksEntities = new ArrayList<>();
    private final AtomicLong userLinksIdGenerator = new AtomicLong(1);

    private List<LinksEntity> linksEntities = new ArrayList<>();
    private final AtomicLong linksEntityIdGenerator = new AtomicLong(1);

    private List<InfoEntity> infoEntities = new ArrayList<>();
    private final AtomicLong infoIdGenerator = new AtomicLong(1);

    private URI getUriById(long linkId) {
        return linksEntities.stream()
            .filter(it -> it.linkId == linkId)
            .map(it -> it.linkUri)
            .findFirst()
            .orElseThrow();
    }

    private OffsetDateTime getLastUpdateTimeById(long linkId) {
        return linksEntities.stream()
            .filter(it -> it.linkId == linkId)
            .map(it -> it.lastUpdateTime)
            .findFirst()
            .orElseThrow();
    }

    private List<String> getTagsById(long infoId) {
        return infoEntities.stream()
            .filter(ie -> ie.infoId == infoId)
            .map(ie -> ie.tags)
            .findFirst()
            .orElseThrow();
    }

    private List<String> getFiltersById(long infoId) {
        return infoEntities.stream()
            .filter(ie -> ie.infoId == infoId)
            .map(ie -> ie.filters)
            .findFirst()
            .orElseThrow();
    }

    private long getLinkIdByUri(URI linkUri) {
        return linksEntities.stream()
            .filter(it -> it.linkUri().equals(linkUri))
            .map(it -> it.linkId)
            .findFirst()
            .orElseThrow();
    }

    private List<URI> getAllLinks() {
        return linksEntities.stream()
            .map(it -> it.linkUri)
            .toList();
    }

    @Override
    public boolean isRegistered(long chatId) {
        return users.contains(chatId);
    }

    @Override
    public void register(long chatId) {
        if (isRegistered(chatId)) {
            return; // Если пользователь уже зарегистрирован, ничего не делаем
        }
        users.add(chatId);
    }

    @Override
    public void unRegister(long chatId) {
        if (!isRegistered(chatId)) {
            return; // Если пользователь не зарегистрирован, ничего не делаем
        }
        users.remove(chatId);
        userLinksEntities.removeIf(userLinksEntity -> userLinksEntity.chatId == chatId);
    }

    @Override
    public Link saveLink(long chatId, Link link) {
// Проверяем, зарегистрирован ли пользователь
        if (!isRegistered(chatId)) {
            throw new IllegalArgumentException("Пользователь не зарегистрирован");
        }

        long linkId = processLinkId(link);
        long infoId = processInfoId(link);

        // Добавляем связь пользователя с сохраненной ссылкой
        long generalId = userLinksIdGenerator.getAndIncrement();
        UserLinksEntity userLink = new UserLinksEntity(generalId, chatId, linkId, infoId);
        userLinksEntities.add(userLink);

        // Возвращаем обновленный объект Link с присвоенным ID
        return new Link(generalId, link.uri(), link.tags(), link.filters(), link.lastUpdateTime());
    }

    private long processLinkId(Link link) {
        Optional<Long> maybeLinkId = linksEntities.stream()
            .filter(it -> it.linkUri().equals(link.uri()))
            .map(it -> it.linkId)
            .findFirst();

        if (maybeLinkId.isEmpty()) {
            long linkId = linksEntityIdGenerator.getAndIncrement();
            linksEntities.add(new LinksEntity(linkId, link.uri(), link.lastUpdateTime()));
            return linkId;
        } else {
            return maybeLinkId.get();
        }
    }

    private long processInfoId(Link link) {
        Optional<Long> maybeInfoId = infoEntities.stream()
            .filter(it -> it.tags.stream().sorted().equals(link.tags().stream().sorted()))
            .filter(it -> it.filters.stream().sorted().equals(link.filters().stream().sorted()))
            .map(it -> it.infoId)
            .findFirst();

        if (maybeInfoId.isEmpty()) {
            long infoId = infoIdGenerator.getAndIncrement();
            infoEntities.add(new InfoEntity(infoId, link.tags(), link.filters()));
            return infoId;
        } else {
            return maybeInfoId.get();
        }
    }

    @Override
    public Optional<Link> deleteLink(long chatId, String url) {
        Optional<UserLinksEntity> maybeDeletedLink = userLinksEntities.stream()
            .filter(userLinksEntity -> userLinksEntity.chatId == chatId)
            .filter(userLinksEntity -> userLinksEntity.linkId == getLinkIdByUri(URI.create(url)))
            .findFirst();

        userLinksEntities.removeIf(
            userLinksEntity ->
                userLinksEntity.chatId == chatId && userLinksEntity.linkId == getLinkIdByUri(URI.create(url))
        );

        if (maybeDeletedLink.isEmpty()) {
            return Optional.empty();
        }
        UserLinksEntity deletedLink = maybeDeletedLink.get();
        return Optional.of(
            new Link(
                deletedLink.id,
                getUriById(deletedLink.linkId),
                getTagsById(deletedLink.infoId),
                getFiltersById(deletedLink.infoId),
                getLastUpdateTimeById(deletedLink.linkId)
            )
        );

    }


    @Override
    public List<Link> findById(long chatId) {
        List<Link> links = new ArrayList<>();
        userLinksEntities.stream()
            .filter(userLinksEntity -> userLinksEntity.chatId == chatId)
            .forEach(userLinksEntity -> {
                Link link = new Link(
                    userLinksEntity.id,
                    getUriById(userLinksEntity.linkId),
                    getTagsById(userLinksEntity.infoId),
                    getFiltersById(userLinksEntity.infoId),
                    getLastUpdateTimeById(userLinksEntity.linkId)
                );
                links.add(link);
            });
        return links;
    }

    @Override
    public List<Long> getAllChatIdByLink(String uri) {
        long uriId = getLinkIdByUri(URI.create(uri));

        return userLinksEntities.stream()
            .filter(userLinksEntity -> userLinksEntity.linkId == uriId)
            .map(userLinksEntity -> userLinksEntity.chatId)
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
        return linksEntities.stream()
            .filter(it -> it.linkUri.getHost().equals(host))
            .map(it -> new LinkMetadata(it.linkId, it.linkUri, it.lastUpdateTime))
            .toList();
    }

    @Override
    public void updateLinksLastUpdateTime(List<LinkMetadata> updatedLinks) {
        updatedLinks.forEach(linkWithNewDate -> {
            linksEntities.stream()
                .filter(linkWithOldDate -> linkWithOldDate.linkId == linkWithNewDate.id())
                .findFirst()
                .ifPresent(linkWithOldDate -> {
                    LinksEntity linksEntity = new LinksEntity(
                        linkWithOldDate.linkId,
                        linkWithOldDate.linkUri,
                        linkWithNewDate.lastUpdateTime()
                    );
                    linksEntities.remove(linkWithOldDate);
                    linksEntities.add(linksEntity);
                });
        });

    }

    private record LinksEntity(
        long linkId,
        URI linkUri,
        OffsetDateTime lastUpdateTime
    ) {
    }

    private record InfoEntity(
        long infoId,
        List<String> tags,
        List<String> filters
    ) {
    }

    private record UserLinksEntity(
        long id,
        long chatId,
        long linkId,
        long infoId
    ) {
    }


}
