package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.models.domain.*;
import backend.academy.scrapper.models.domain.ids.*;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.*;

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
                    new ChatId(rs.getLong("user_chat_id"))
                );

                Link link = new Link(
                    new LinkId(rs.getLong("link_id")),
                    URI.create(rs.getString("link_uri")),
                    rs.getObject("link_last_modified_date", OffsetDateTime.class)
                );

                subscription = Subscription.builder()
                    .subscriptionId(new SubscriptionId(subId))
                    .user(user)
                    .link(link)
                    .tags(new ArrayList<>())
                    .filters(new ArrayList<>())
                    .build();

                map.put(subId, subscription);
            }

            // Добавляем тег, если есть
            long tagId = rs.getLong("tag_id");
            if (!rs.wasNull()) {
                Tag tag = new Tag(new TagId(tagId), rs.getString("tag_value"));
                if (!subscription.tags().contains(tag)) {
                    subscription.tags().add(tag);
                }
            }

            // Добавляем фильтр, если есть
            long filterId = rs.getLong("filter_id");
            if (!rs.wasNull()) {
                Filter filter = new Filter(new FilterId(filterId), rs.getString("filter_value"));
                if (!subscription.filters().contains(filter)) {
                    subscription.filters().add(filter);
                }
            }
        }

        return new ArrayList<>(map.values());
    }
}

