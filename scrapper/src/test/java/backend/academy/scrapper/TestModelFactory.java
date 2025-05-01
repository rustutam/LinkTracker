package backend.academy.scrapper;

import backend.academy.scrapper.models.domain.*;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.models.domain.ids.UserId;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Утилитный класс для генерации тестовых доменных моделей.
 */
public class TestModelFactory {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private static long nextId() {
        return COUNTER.incrementAndGet();
    }

    public static User createUser() {
        long id = nextId();
        return new User(
            new UserId(id),
            new ChatId(id),
            OffsetDateTime.now()
        );
    }

    public static Link createLink() {
        long id = nextId();
        return new Link(
            new LinkId(id),
            URI.create("https://github.com//resource/" + id),
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );
    }

    public static Tag createTag() {
        long id = nextId();
        return new Tag(
            new TagId(id),
            "tag-" + id,
            OffsetDateTime.now()
        );
    }

    public static Filter createFilter() {
        long id = nextId();
        return new Filter(
            new FilterId(id),
            "value-" + id,
            OffsetDateTime.now()
        );
    }

    public static List<Tag> createTags(int count) {
        return IntStream.rangeClosed(1, count)
            .mapToObj(i -> createTag())
            .collect(Collectors.toList());
    }

    public static List<Filter> createFilters(int count) {
        return IntStream.rangeClosed(1, count)
            .mapToObj(i -> createFilter())
            .collect(Collectors.toList());
    }

    public static Subscription createSubscription() {
        User user = createUser();
        Link link = createLink();
        List<Tag> tags = createTags(2);
        List<Filter> filters = createFilters(2);
        long id = nextId();
        return new Subscription(
            new SubscriptionId(id),
            user,
            link,
            new ArrayList<>(tags),
            new ArrayList<>(filters),
            OffsetDateTime.now()
        );
    }

    /**
     * Создает подписку с заданными частями, остальные поля генерируются автоматически.
     */
    public static Subscription createSubscription(
        User user,
        Link link,
        List<Tag> tags,
        List<Filter> filters
    ) {
        long id = nextId();
        return new Subscription(
            new SubscriptionId(id),
            user,
            link,
            tags != null ? new ArrayList<>(tags) : createTags(1),
            filters != null ? new ArrayList<>(filters) : createFilters(1),
            OffsetDateTime.now()
        );
    }
}

