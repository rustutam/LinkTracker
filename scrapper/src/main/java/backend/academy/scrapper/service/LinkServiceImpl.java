package backend.academy.scrapper.service;

import backend.academy.scrapper.exceptions.AlreadyTrackLinkException;
import backend.academy.scrapper.exceptions.NotExistUserException;
import backend.academy.scrapper.exceptions.NotTrackLinkException;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkMetadata;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.repository.database.LinkRepository;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import backend.academy.scrapper.repository.database.UserRepository;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final UserRepository userRepository;
    private final LinkRepository linkRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public LinkMetadata addLink(ChatId chatId, LinkMetadata linkMetadata) {
        // Проверяем что пользователь существует
        User user = userRepository.findByChatId(chatId)
            .orElseThrow(NotExistUserException::new);

        // Проверяем что пользователь не подписан на эту ссылку
        subscriptionRepository.findByUser(user)
            .stream()
            .filter(s -> s.link().uri().toString().equals(linkMetadata.link().uri().toString()))
            .findFirst()
            .ifPresent(l -> {
                throw new AlreadyTrackLinkException();
            });

        // Сохраняем подписываем пользователя на ссылку
        Subscription subscription = new Subscription(
            User.of(chatId),
            linkMetadata.link(),
            linkMetadata.tags(),
            linkMetadata.filters()
        );

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        return LinkMetadata.builder()
            .link(savedSubscription.link())
            .tags(savedSubscription.tags())
            .filters(savedSubscription.filters())
            .build();
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
