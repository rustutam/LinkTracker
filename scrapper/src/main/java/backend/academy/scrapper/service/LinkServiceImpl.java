package backend.academy.scrapper.service;

import backend.academy.scrapper.exceptions.AlreadyTrackLinkException;
import backend.academy.scrapper.exceptions.NotExistUserException;
import backend.academy.scrapper.exceptions.NotTrackLinkException;
import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkMetadata;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.repository.database.FilterRepository;
import backend.academy.scrapper.repository.database.LinkRepository;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import backend.academy.scrapper.repository.database.TagRepository;
import backend.academy.scrapper.repository.database.UserRepository;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final UserRepository userRepository;
    private final LinkRepository linkRepository;
    private final TagRepository tagRepository;
    private final FilterRepository filterRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public LinkMetadata addLink(ChatId chatId, URI uri, List<String> tags, List<String> filters) {
        // Проверяем что пользователь существует
        User savedUser = userRepository.findByChatId(chatId)
            .orElseThrow(NotExistUserException::new);

        // Проверяем что пользователь не подписан на эту ссылку
        subscriptionRepository.findByUser(savedUser)
            .stream()
            .filter(s -> s.link().uri().toString().equals(uri.toString()))
            .findFirst()
            .ifPresent(l -> {
                throw new AlreadyTrackLinkException();
            });

        // Получаем ссылку из бд
        Link savedLink = processLink(uri);

        // Получаем список тегов из бд
        List<Tag> savedTags = processTags(tags);

        // Получаем список фильтров из бд
        List<Filter> savedFilters = processFilters(filters);

        Subscription subscription = Subscription.builder()
            .link(savedLink)
            .tags(savedTags)
            .filters(savedFilters)
            .build();

        // Подписываем пользователя на ссылку с тегами и фильтрами
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        return LinkMetadata.builder()
            .link(savedSubscription.link())
            .tags(savedSubscription.tags())
            .filters(savedSubscription.filters())
            .build();
    }

    private Link processLink(URI uri) {
        return linkRepository.findByUri(uri)
            .orElseGet(() -> linkRepository.save(uri));
    }

    private List<Tag> processTags(List<String> tags) {
        return tags.stream()
            .map(tagValue -> tagRepository.findByTag(tagValue)
                .orElseGet(() -> tagRepository.save(tagValue)))
            .collect(Collectors.toList());
    }

    private List<Filter> processFilters(List<String> filters) {
        return filters.stream()
            .map(filterValue -> filterRepository.findByFilter(filterValue)
                .orElseGet(() -> filterRepository.save(filterValue)))
            .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public LinkMetadata removeLink(ChatId chatId, URI uri) {
        // Проверяем что пользователь существует
        User user = userRepository.findByChatId(chatId)
            .orElseThrow(NotExistUserException::new);

        // Проверяем что ссылка существует
        // Если такого Link не существует - это означает что пользователь не подписан на эту ссылку
        Link link = linkRepository.findByUri(uri)
            .orElseThrow(NotTrackLinkException::new);

        // Получаем подписку,
        // Проверяем что пользователь подписан на эту ссылку
        Subscription subscription = subscriptionRepository.findByUserAndLink(user, link)
            .stream()
            .findFirst()
            .orElseThrow(NotTrackLinkException::new);

        // Удаляем подписку на ссылку
        subscriptionRepository.remove(subscription);

        return LinkMetadata.builder()
            .link(subscription.link())
            .tags(subscription.tags())
            .filters(subscription.filters())
            .build();
    }

    @Override
    public List<LinkMetadata> getLinks(ChatId chatId) {
        // Проверяем что пользователь существует
        User user = userRepository.findByChatId(chatId)
            .orElseThrow(NotExistUserException::new);

        // Получаем все подписки пользователя
        return subscriptionRepository.findByUser(user)
            .stream()
            .map(s ->
                new LinkMetadata(
                    s.link(),
                    s.tags(),
                    s.filters()
                )
            )
            .toList();
    }
}
