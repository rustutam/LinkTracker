package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.exceptions.AlreadyTrackLinkException;
import backend.academy.scrapper.exceptions.NotExistLinkException;
import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.exceptions.NotTrackLinkException;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.entities.LinkEntity;
import backend.academy.scrapper.models.entities.UserEntity;
import backend.academy.scrapper.repository.database.LinkRepository;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import backend.academy.scrapper.repository.database.ChatRepository;
import backend.academy.scrapper.repository.database.utilities.JdbcRowMapperUtil;
import backend.academy.scrapper.repository.database.utilities.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcSubscriptionRepository implements SubscriptionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;

    @Override
    public Subscription save(User user, Link link) {
        User findUser = getUser(user);
        Link createdLink = linkRepository.save(link.uri());

        SubscriptionId subscriptionId = new SubscriptionId(
            jdbcTemplate.queryForObject(
                "INSERT INTO subscriptions (user_id, link_id) VALUES (?, ?) RETURNING id",
                Long.class,
                findUser.userId().id(),
                createdLink.linkId().id()
            )
        );

        return Subscription.builder()
            .subscriptionId(subscriptionId)
            .user(findUser)
            .link(createdLink)
            .build();
    }

    @Override
    public Subscription remove(User user, Link link) {
        User findUser = getUser(user);
        Link findLink = linkRepository.findByUri(link.uri()).orElseThrow(NotTrackLinkException::new);

        SubscriptionId subscriptionId = new SubscriptionId(
            jdbcTemplate.queryForObject(
                "DELETE FROM subscriptions WHERE user_id =? AND link_id =? RETURNING id",
                Long.class,
                findUser.userId().id(),
                findLink.linkId().id()
            )
        );

        return Subscription.builder()
            .subscriptionId(subscriptionId)
            .user(findUser)
            .link(findLink)
            .build();

    }

    @Override
    public List<Link> findAllLinksByChatId(ChatId chatId) {
        return List.of();
    }

    @Override
    public List<ChatId> findAllChatIdsByLinkId(LinkId linkId) {
        return List.of();
    }

    @Override
    public Optional<Subscription> findById(SubscriptionId subscriptionId) {
        var userEntities = jdbcTemplate.query(
            "SELECT * FROM users WHERE chat_id = (?)",
            JdbcRowMapperUtil::mapRowToUserEntity,
            subscriptionId.id()
        );

        return userEntities.stream().map(UserMapper::toDomain).findFirst();
    }

    private User getUser(User user) {
        return chatRepository.findByChatId(user.chatId()).orElseThrow(NotExistTgChatException::new);
    }
}
