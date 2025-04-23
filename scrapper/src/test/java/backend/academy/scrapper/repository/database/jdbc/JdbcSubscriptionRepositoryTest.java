package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.models.domain.ids.UserId;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
class JdbcSubscriptionRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcSubscriptionRepository jdbcSubscriptionRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveSubscriptionTest() {
        UserId userId = new UserId(1L);
        LinkId linkId = new LinkId(6L);

        SubscriptionId savedSubscriptionId = jdbcSubscriptionRepository.save(userId, linkId);

        assertNotNull(savedSubscriptionId);
        assertTrue(jdbcSubscriptionRepository.findById(savedSubscriptionId).isPresent());
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void removeSubscriptionTest() {
        UserId userId = new UserId(2L);
        LinkId linkId = new LinkId(4L);

        Subscription removedSubscription = jdbcSubscriptionRepository.remove(userId, linkId);

        assertEquals(removedSubscription.userId(), userId);
        assertEquals(removedSubscription.linkId(), linkId);
        assertTrue(jdbcSubscriptionRepository.findById(removedSubscription.subscriptionId()).isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllLinksByChatIdTest() {
        ChatId firstChatId = new ChatId(100L);
        ChatId secondChatId = new ChatId(101L);
        ChatId thirdChatId = new ChatId(102L);

        URI linkUri1 = URI.create("https://github.com/java-rustutam/semester1");
        URI linkUri2 = URI.create("https://github.com/java-rustutam/semester2");
        URI linkUri3 = URI.create("https://github.com/java-rustutam/semester3");
        URI linkUri4 = URI.create("https://github.com/java-rustutam/semester4");
        URI linkUri5 = URI.create("https://github.com/java-rustutam/semester5");

        OffsetDateTime linkLastModifiedDate1 = OffsetDateTime.parse("2024-01-01 10:00:00", formatter.withZone(ZoneOffset.UTC));
        OffsetDateTime linkLastModifiedDate2 = OffsetDateTime.parse("2024-01-02 10:00:00", formatter.withZone(ZoneOffset.UTC));
        OffsetDateTime linkLastModifiedDate3 = OffsetDateTime.parse("2024-01-03 10:00:00", formatter.withZone(ZoneOffset.UTC));
        OffsetDateTime linkLastModifiedDate4 = OffsetDateTime.parse("2024-01-04 10:00:00", formatter.withZone(ZoneOffset.UTC));
        OffsetDateTime linkLastModifiedDate5 = OffsetDateTime.parse("2024-01-05 10:00:00", formatter.withZone(ZoneOffset.UTC));


        List<Link> expectedFirstUserLinks = List.of(
            new Link(new LinkId(1L), linkUri1, linkLastModifiedDate1),
            new Link(new LinkId(2L), linkUri2, linkLastModifiedDate2),
            new Link(new LinkId(3L), linkUri3, linkLastModifiedDate3)
        );


        List<Link> expectedSecondUserLinks = List.of(
            new Link(new LinkId(4L), linkUri4, linkLastModifiedDate4),
            new Link(new LinkId(5L), linkUri5, linkLastModifiedDate5)
        );

        List<Link> firstUserLinks = jdbcSubscriptionRepository.findAllLinksByChatId(firstChatId);
        List<Link> secondUserLinks = jdbcSubscriptionRepository.findAllLinksByChatId(secondChatId);
        List<Link> thirdUserLinks = jdbcSubscriptionRepository.findAllLinksByChatId(thirdChatId);

        assertEquals(expectedFirstUserLinks, firstUserLinks);
        assertEquals(expectedSecondUserLinks, secondUserLinks);
        assertTrue(thirdUserLinks.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllChatIdsByLinkIdTest() {
        LinkId firstLinkId = new LinkId(1L);
        LinkId secondLinkId = new LinkId(2L);
        LinkId sixthLinkId = new LinkId(6L);

        List<ChatId> expectedFirstLinkChatIds = List.of(
            new ChatId(100L)
        );
        List<ChatId> expectedSecondLinkChatIds = List.of(
            new ChatId(100L),
            new ChatId(103L),
            new ChatId(104L)
        );


        List<ChatId> firstLinkChatIds = jdbcSubscriptionRepository.findAllChatIdsByLinkId(firstLinkId);
        List<ChatId> secondLinkChatIds = jdbcSubscriptionRepository.findAllChatIdsByLinkId(secondLinkId);
        List<ChatId> sixthLinkChatIds = jdbcSubscriptionRepository.findAllChatIdsByLinkId(sixthLinkId);

        assertEquals(expectedFirstLinkChatIds, firstLinkChatIds);
        assertEquals(expectedSecondLinkChatIds, secondLinkChatIds);
        assertTrue(sixthLinkChatIds.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdTest() {
        SubscriptionId subscriptionId = new SubscriptionId(2L);
        UserId userId = new UserId(1L);
        LinkId linkId = new LinkId(2L);

        Optional<Subscription> subscription = jdbcSubscriptionRepository.findById(subscriptionId);

        assertTrue(subscription.isPresent());
        assertEquals(subscriptionId, subscription.get().subscriptionId());
        assertEquals(userId, subscription.get().userId());
        assertEquals(linkId, subscription.get().linkId());
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdWhenSubscriptionNotFoundTest() {
        SubscriptionId subscriptionId = new SubscriptionId(100L);

        Optional<Subscription> subscription = jdbcSubscriptionRepository.findById(subscriptionId);

        assertTrue(subscription.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByLinkIdAndUserId() {
        SubscriptionId subscriptionId = new SubscriptionId(2L);
        UserId userId = new UserId(1L);
        LinkId linkId = new LinkId(2L);

        Optional<Subscription> subscription = jdbcSubscriptionRepository.findByLinkIdAndUserId(linkId, userId);

        assertTrue(subscription.isPresent());
        assertEquals(subscriptionId, subscription.get().subscriptionId());
        assertEquals(userId, subscription.get().userId());
        assertEquals(linkId, subscription.get().linkId());
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByLinkIdAndUserIdWhenSubscriptionNotFoundTest() {
        UserId userId = new UserId(10L);
        LinkId linkId = new LinkId(2L);

        Optional<Subscription> subscription = jdbcSubscriptionRepository.findByLinkIdAndUserId(linkId, userId);

        assertTrue(subscription.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addFilterToSubscriptionTest() {
        SubscriptionId subscriptionId = new SubscriptionId(6L);
        FilterId filterId = new FilterId(5L);

        jdbcSubscriptionRepository.addFilterToSubscription(subscriptionId, filterId);

        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM scrapper.subscription_filters WHERE subscription_id = ? AND filter_id = ?",
            Integer.class,
            subscriptionId.id(),
            filterId.id()
        );

        assertNotNull(count);
        assertEquals(1, count);
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void removeFilterFromSubscriptionTest() {
        SubscriptionId subscriptionId = new SubscriptionId(1L);
        FilterId filterId = new FilterId(1L);

        // Проверяем что есть строка
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM scrapper.subscription_filters WHERE subscription_id = ? AND filter_id = ?",
            Integer.class,
            subscriptionId.id(),
            filterId.id()
        );

        assertNotNull(count);
        assertEquals(1, count);

        jdbcSubscriptionRepository.removeFilterFromSubscription(subscriptionId, filterId);

        Integer mutationCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM scrapper.subscription_filters WHERE subscription_id = ? AND filter_id = ?",
            Integer.class,
            subscriptionId.id(),
            filterId.id()
        );

        assertNotNull(mutationCount);
        assertEquals(0, mutationCount);
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findFiltersBySubscriptionIdTest() {
        SubscriptionId subscriptionId = new SubscriptionId(1L);
        List<Filter> expectedFilters = List.of(
            new Filter(new FilterId(1L), "filter1"),
            new Filter(new FilterId(3L), "filter3")
        );

        List<Filter> filter = jdbcSubscriptionRepository.findFiltersBySubscriptionId(subscriptionId);

        assertEquals(2, filter.size());
        assertEquals(expectedFilters, filter);
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addTagToSubscriptionTest() {
        SubscriptionId subscriptionId = new SubscriptionId(6L);
        TagId tagId = new TagId(5L);

        jdbcSubscriptionRepository.addTagToSubscription(subscriptionId, tagId);

        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM scrapper.subscription_tags WHERE subscription_id = ? AND tag_id = ?",
            Integer.class,
            subscriptionId.id(),
            tagId.id()
        );

        assertNotNull(count);
        assertEquals(1, count);
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void removeTagFromSubscriptionTest() {
        SubscriptionId subscriptionId = new SubscriptionId(1L);
        TagId tagId = new TagId(1L);

        // Проверяем что есть строка
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM scrapper.subscription_tags WHERE subscription_id = ? AND tag_id = ?",
            Integer.class,
            subscriptionId.id(),
            tagId.id()
        );

        assertNotNull(count);
        assertEquals(1, count);

        jdbcSubscriptionRepository.removeTagFromSubscription(subscriptionId, tagId);

        Integer mutationCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM scrapper.subscription_tags WHERE subscription_id = ? AND tag_id = ?",
            Integer.class,
            subscriptionId.id(),
            tagId.id()
        );

        assertNotNull(mutationCount);
        assertEquals(0, mutationCount);
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findTagsBySubscriptionIdTest() {
        SubscriptionId subscriptionId = new SubscriptionId(1L);
        List<Tag> expectedTags = List.of(
            new Tag(new TagId(1L), "tag1"),
            new Tag(new TagId(3L), "tag3")
        );

        List<Tag> tag = jdbcSubscriptionRepository.findTagsBySubscriptionId(subscriptionId);

        assertEquals(2, tag.size());
        assertEquals(expectedTags, tag);
    }
}
