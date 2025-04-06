package backend.academy.scrapper.service.jdbc;

import backend.academy.scrapper.exceptions.AlreadyTrackLinkException;
import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.exceptions.NotTrackLinkException;
import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkMetadata;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.repository.database.*;
import backend.academy.scrapper.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcLinkService implements LinkService {
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;
    private final TagRepository tagRepository;
    private final FilterRepository filterRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final LinkMetadataRepository linkMetadataRepository;

    @Override
    @Transactional
    public LinkMetadata addLink(ChatId chatId, LinkMetadata linkMetadata) {
        User user = chatRepository.findByChatId(chatId)
            .orElseThrow(NotExistTgChatException::new);

        if (linkRepository.findByUri(linkMetadata.link().uri())
            .filter(l -> subscriptionRepository.findAllLinksByChatId(user.chatId()).contains(l))
            .isPresent()) {
            throw new AlreadyTrackLinkException();
        }

        Link savedLink = linkRepository.save(linkMetadata.link().uri());
        SubscriptionId savedSubscriptionId = subscriptionRepository.save(user.userId(), savedLink.linkId());

        List<Tag> savedTags = linkMetadata.tags().stream()
            .map(tag -> {
                Tag savedTag = tagRepository.save(tag.value());
                subscriptionRepository.addTagToSubscription(savedSubscriptionId, savedTag.tagId());
                return savedTag;
            }).toList();

        List<Filter> savedFilters = linkMetadata.filters().stream()
            .map(filter -> {
                Filter savedFilter = filterRepository.save(filter.value());
                subscriptionRepository.addFilterToSubscription(savedSubscriptionId, savedFilter.filterId());
                return savedFilter;
            }).toList();

        return LinkMetadata.builder()
            .link(savedLink)
            .tags(savedTags)
            .filters(savedFilters)
            .build();
    }


    @Override
    @Transactional
    public LinkMetadata removeLink(ChatId chatId, URI uri) {
        User user = chatRepository.findByChatId(chatId)
            .orElseThrow(NotExistTgChatException::new);

        Link link = linkRepository.findByUri(uri)
            .filter(l -> subscriptionRepository.findAllLinksByChatId(user.chatId()).contains(l))
            .orElseThrow(NotTrackLinkException::new);

        Subscription subscription = subscriptionRepository.findByLinkIdAndUserId(link.linkId(), user.userId())
            .orElseThrow();

        subscriptionRepository.remove(user, link);

        return LinkMetadata.builder()
            .link(link)
            .tags(subscriptionRepository.findTagsBySubscriptionId(subscription.subscriptionId()))
            .filters(subscriptionRepository.findFiltersBySubscriptionId(subscription.subscriptionId()))
            .build();
    }

    @Override
    public List<LinkMetadata> getLinks(ChatId chatId) {
        return linkMetadataRepository.findAllLinkMetadataByChatId(chatId);
    }
}
