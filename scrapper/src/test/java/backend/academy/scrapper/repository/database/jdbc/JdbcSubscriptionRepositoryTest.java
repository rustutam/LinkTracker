package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.TestUtils;
import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.models.domain.ids.UserId;
import backend.academy.scrapper.repository.database.FilterRepository;
import backend.academy.scrapper.repository.database.LinkRepository;
import backend.academy.scrapper.repository.database.TagRepository;
import backend.academy.scrapper.repository.database.UserRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = "app.access-type=SQL")
class JdbcSubscriptionRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcSubscriptionRepository jdbcSubscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RecursiveComparisonConfiguration config;

    @BeforeEach
    void setUp() {
        config = TestUtils.CONFIG();
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveSubscriptionTest() {
        User savedUser = userRepository.findById(new UserId(1L)).orElseThrow();
        Link savedLink = linkRepository.findById(new LinkId(6L)).orElseThrow();
        List<Tag> savedTags = List.of(
            tagRepository.findById(new TagId(1L)).orElseThrow(),
            tagRepository.findById(new TagId(3L)).orElseThrow(),
            tagRepository.findById(new TagId(4L)).orElseThrow()
        );
        List<Filter> savedFilters = List.of(
            filterRepository.findById(new FilterId(1L)).orElseThrow(),
            filterRepository.findById(new FilterId(5L)).orElseThrow()
        );


        Subscription subscription = Subscription.builder()
            .user(savedUser)
            .link(savedLink)
            .tags(savedTags)
            .filters(savedFilters)
            .build();


        Subscription savedSubscription = jdbcSubscriptionRepository.save(subscription);

        assertNotNull(savedSubscription.subscriptionId());
        assertThat(savedSubscription)
            .usingRecursiveComparison(config)
            .ignoringFields("subscriptionId", "createdAt")
            .isEqualTo(subscription);

        Subscription result = jdbcSubscriptionRepository.findById(savedSubscription.subscriptionId()).orElseThrow();
        assertThat(savedSubscription)
            .usingRecursiveComparison(config)
            .isEqualTo(result);
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void removeSubscriptionTest() {
        User savedUser = userRepository.findById(new UserId(1L)).orElseThrow();
        Link savedLink = linkRepository.findById(new LinkId(1L)).orElseThrow();
        List<Tag> savedTags = List.of(
            tagRepository.findById(new TagId(1L)).orElseThrow(),
            tagRepository.findById(new TagId(3L)).orElseThrow()
        );
        List<Filter> savedFilters = List.of(
            filterRepository.findById(new FilterId(1L)).orElseThrow(),
            filterRepository.findById(new FilterId(3L)).orElseThrow()
        );

        Subscription subscription = Subscription.builder()
            .subscriptionId(new SubscriptionId(1L))
            .user(savedUser)
            .link(savedLink)
            .tags(savedTags)
            .filters(savedFilters)
            .build();


        Subscription deletedSubscription = jdbcSubscriptionRepository.remove(subscription);

        assertNotNull(deletedSubscription);
        assertThat(deletedSubscription)
            .usingRecursiveComparison(config)
            .isEqualTo(subscription);

        assertTrue(jdbcSubscriptionRepository.findById(deletedSubscription.subscriptionId()).isEmpty());

        Integer tagsCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM scrapper.subscription_tags WHERE subscription_id = ?",
            Integer.class,
            subscription.subscriptionId().id()
        );

        Integer filtersCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM scrapper.subscription_filters WHERE subscription_id = ?",
            Integer.class,
            subscription.subscriptionId().id()
        );

        assertThat(tagsCount).isZero();
        assertThat(filtersCount).isZero();
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdTest() {
        User savedUser = userRepository.findById(new UserId(1L)).orElseThrow();
        Link savedLink = linkRepository.findById(new LinkId(1L)).orElseThrow();
        List<Tag> savedTags = List.of(
            tagRepository.findById(new TagId(1L)).orElseThrow(),
            tagRepository.findById(new TagId(3L)).orElseThrow()
        );
        List<Filter> savedFilters = List.of(
            filterRepository.findById(new FilterId(1L)).orElseThrow(),
            filterRepository.findById(new FilterId(3L)).orElseThrow()
        );

        Subscription expectedSubscription = Subscription.builder()
            .subscriptionId(new SubscriptionId(1L))
            .user(savedUser)
            .link(savedLink)
            .tags(savedTags)
            .filters(savedFilters)
            .createdAt(OffsetDateTime.parse("2024-01-02T10:00:00Z"))
            .build();

        Subscription actualSubscription = jdbcSubscriptionRepository.findById(new SubscriptionId(1L)).orElse(null);

        assertNotNull(actualSubscription);
        assertThat(actualSubscription)
            .usingRecursiveComparison(config)
            .isEqualTo(expectedSubscription);
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
    void findByUserAndLinkTest() {
        User savedUser = userRepository.findById(new UserId(1L)).orElseThrow();
        Link savedLink = linkRepository.findById(new LinkId(1L)).orElseThrow();
        List<Tag> savedTags = List.of(
            tagRepository.findById(new TagId(1L)).orElseThrow(),
            tagRepository.findById(new TagId(3L)).orElseThrow()
        );
        List<Filter> savedFilters = List.of(
            filterRepository.findById(new FilterId(1L)).orElseThrow(),
            filterRepository.findById(new FilterId(3L)).orElseThrow()
        );

        Subscription expectedSubscription = Subscription.builder()
            .subscriptionId(new SubscriptionId(1L))
            .user(savedUser)
            .link(savedLink)
            .tags(savedTags)
            .filters(savedFilters)
            .createdAt(OffsetDateTime.parse("2024-01-02T10:00:00Z"))
            .build();

        Subscription actualSubscription =
            jdbcSubscriptionRepository.findByUserAndLink(savedUser, savedLink).orElse(null);

        assertNotNull(actualSubscription);
        assertThat(actualSubscription)
            .usingRecursiveComparison(config)
            .isEqualTo(expectedSubscription);
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserAndLinkWhenSubscriptionNotFoundTest() {
        User savedUser = userRepository.findById(new UserId(1L)).orElseThrow();
        Link savedLink = linkRepository.findById(new LinkId(6L)).orElseThrow();

        Optional<Subscription> subscription = jdbcSubscriptionRepository.findByUserAndLink(savedUser, savedLink);

        assertTrue(subscription.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserTest() {
        User savedUser = userRepository.findById(new UserId(1L)).orElseThrow();

        Link savedLink1 = linkRepository.findById(new LinkId(1L)).orElseThrow();
        List<Tag> savedTags1 = List.of(
            tagRepository.findById(new TagId(1L)).orElseThrow(),
            tagRepository.findById(new TagId(3L)).orElseThrow()
        );
        List<Filter> savedFilters1 = List.of(
            filterRepository.findById(new FilterId(1L)).orElseThrow(),
            filterRepository.findById(new FilterId(3L)).orElseThrow()
        );

        Subscription firstSub = Subscription.builder()
            .subscriptionId(new SubscriptionId(1L))
            .user(savedUser)
            .link(savedLink1)
            .tags(savedTags1)
            .filters(savedFilters1)
            .createdAt(OffsetDateTime.parse("2024-01-02T10:00:00Z"))
            .build();


        Link savedLink2 = linkRepository.findById(new LinkId(2L)).orElseThrow();
        List<Tag> savedTags2 = List.of(
            tagRepository.findById(new TagId(2L)).orElseThrow()
        );
        List<Filter> savedFilters2 = List.of(
            filterRepository.findById(new FilterId(2L)).orElseThrow()
        );

        Subscription secondSub = Subscription.builder()
            .subscriptionId(new SubscriptionId(2L))
            .user(savedUser)
            .link(savedLink2)
            .tags(savedTags2)
            .filters(savedFilters2)
            .createdAt(OffsetDateTime.parse("2024-01-03T10:00:00Z"))
            .build();

        Link savedLink3 = linkRepository.findById(new LinkId(3L)).orElseThrow();

        Subscription thirdSub = Subscription.builder()
            .subscriptionId(new SubscriptionId(3L))
            .user(savedUser)
            .link(savedLink3)
            .tags(List.of())
            .filters(List.of())
            .createdAt(OffsetDateTime.parse("2024-01-12T10:00:00Z"))
            .build();
        List<Subscription> expectedSubscriptions = List.of(firstSub, secondSub, thirdSub);

        List<Subscription> actualSubscriptions = jdbcSubscriptionRepository.findByUser(savedUser);

        assertFalse(actualSubscriptions.isEmpty());
        assertThat(actualSubscriptions)
            .usingRecursiveFieldByFieldElementComparator(config)
            .containsExactlyInAnyOrderElementsOf(expectedSubscriptions);
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserWhenSubscriptionsNotFoundTest() {
        User user = new User(
            new UserId(13L),
            new ChatId(23L),
            OffsetDateTime.MIN
        );

        List<Subscription> subscriptions = jdbcSubscriptionRepository.findByUser(user);

        assertTrue(subscriptions.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserWhenUserFoundAndSubscriptionsNotFoundTest() {
        User savedUser = userRepository.findById(new UserId(3L)).orElseThrow();

        List<Subscription> subscriptions = jdbcSubscriptionRepository.findByUser(savedUser);

        assertTrue(subscriptions.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByLinkTest() {
        Link savedLink = linkRepository.findById(new LinkId(1L)).orElseThrow();

        User savedUser1 = userRepository.findById(new UserId(1L)).orElseThrow();
        List<Tag> savedTags1 = List.of(
            tagRepository.findById(new TagId(1L)).orElseThrow(),
            tagRepository.findById(new TagId(3L)).orElseThrow()
        );
        List<Filter> savedFilters1 = List.of(
            filterRepository.findById(new FilterId(1L)).orElseThrow(),
            filterRepository.findById(new FilterId(3L)).orElseThrow()
        );

        Subscription firstSub = Subscription.builder()
            .subscriptionId(new SubscriptionId(1L))
            .user(savedUser1)
            .link(savedLink)
            .tags(savedTags1)
            .filters(savedFilters1)
            .createdAt(OffsetDateTime.parse("2024-01-02T10:00:00Z"))
            .build();

        User savedUser2 = userRepository.findById(new UserId(4L)).orElseThrow();

        Subscription secondSub = Subscription.builder()
            .subscriptionId(new SubscriptionId(6L))
            .user(savedUser2)
            .link(savedLink)
            .tags(List.of())
            .filters(List.of())
            .createdAt(OffsetDateTime.parse("2024-01-12T10:00:00Z"))
            .build();

        User savedUser3 = userRepository.findById(new UserId(5L)).orElseThrow();

        Subscription thirdSub = Subscription.builder()
            .subscriptionId(new SubscriptionId(7L))
            .user(savedUser3)
            .link(savedLink)
            .tags(List.of())
            .filters(List.of())
            .createdAt(OffsetDateTime.parse("2024-01-01T10:00:00Z"))
            .build();

        List<Subscription> expectedSubscriptions = List.of(firstSub, secondSub, thirdSub);


        List<Subscription> actualSubscriptions = jdbcSubscriptionRepository.findByLink(savedLink);


        assertFalse(actualSubscriptions.isEmpty());
        assertThat(actualSubscriptions)
            .usingRecursiveFieldByFieldElementComparator(config)
            .containsExactlyInAnyOrderElementsOf(expectedSubscriptions);
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByLinkWhenSubscriptionsNotFoundTest() {
        Link link = new Link(
            new LinkId(13L),
            URI.create("abracadabra"),
            OffsetDateTime.MIN,
            OffsetDateTime.MIN
        );

        List<Subscription> subscriptions = jdbcSubscriptionRepository.findByLink(link);

        assertTrue(subscriptions.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByLinkWhenUserFoundAndSubscriptionsNotFoundTest() {
        Link savedLink = linkRepository.findById(new LinkId(6L)).orElseThrow();

        List<Subscription> subscriptions = jdbcSubscriptionRepository.findByLink(savedLink);

        assertTrue(subscriptions.isEmpty());
    }


}
