package backend.academy.scrapper.repository.database.jdbc.mapper;

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
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionResultSetExtractor implements ResultSetExtractor<List<Subscription>> {

    @Override
    public List<Subscription> extractData(ResultSet rs) throws SQLException {
        // Используем LinkedHashMap, чтобы сохранить порядок
        Map<Long, Subscription> map = new LinkedHashMap<>();

        while (rs.next()) {
            long subId = rs.getLong("subscription_id");

            // Если подписка ещё не создана — создаём её и наполняем user/link
            Subscription subscription = map.get(subId);
            if (subscription == null) {
                User user = new User(
                        new UserId(rs.getLong("user_id")),
                        new ChatId(rs.getLong("user_chat_id")),
                        rs.getObject("user_created_at", OffsetDateTime.class));

                Link link = new Link(
                        new LinkId(rs.getLong("link_id")),
                        URI.create(rs.getString("link_uri")),
                        rs.getObject("link_last_modified_date", OffsetDateTime.class),
                        rs.getObject("link_created_at", OffsetDateTime.class));

                subscription = Subscription.builder()
                        .subscriptionId(new SubscriptionId(subId))
                        .user(user)
                        .link(link)
                        .tags(new ArrayList<>())
                        .filters(new ArrayList<>())
                        .createdAt(rs.getObject("subscription_created_at", OffsetDateTime.class))
                        .build();

                map.put(subId, subscription);
            }

            // Добавляем тег, если есть
            long tagId = rs.getLong("tag_id");
            if (!rs.wasNull()) {
                Tag tag = new Tag(
                        new TagId(tagId),
                        rs.getString("tag_value"),
                        rs.getObject("tag_created_at", OffsetDateTime.class));
                if (!subscription.tags().contains(tag)) {
                    subscription.tags().add(tag);
                }
            }

            // Добавляем фильтр, если есть
            long filterId = rs.getLong("filter_id");
            if (!rs.wasNull()) {
                Filter filter = new Filter(
                        new FilterId(filterId),
                        rs.getString("filter_value"),
                        rs.getObject("filter_created_at", OffsetDateTime.class));
                if (!subscription.filters().contains(filter)) {
                    subscription.filters().add(filter);
                }
            }
        }

        return new ArrayList<>(map.values());
    }
}
