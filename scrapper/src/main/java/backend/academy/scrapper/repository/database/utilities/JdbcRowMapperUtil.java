package backend.academy.scrapper.repository.database.utilities;

import backend.academy.scrapper.models.dto.FilterDto;
import backend.academy.scrapper.models.dto.LinkDto;
import backend.academy.scrapper.models.dto.SubscriptionDto;
import backend.academy.scrapper.models.dto.TagDto;
import backend.academy.scrapper.models.dto.UserDto;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import java.sql.ResultSet;
import java.time.OffsetDateTime;

@SuppressWarnings("MultipleStringLiterals")
@NoArgsConstructor
public final class JdbcRowMapperUtil {
    private static final String COLUMN_ID = "id";
    private static final String LINK_TABLE_COLUMN_URI = "uri";
    private static final String LINK_TABLE_COLUMN_LAST_MODIFYING_DATE = "last_modified_date";
    private static final String COLUMN_CREATE_AT = "created_at";

    private static final String TAG_TABLE_COLUMN_TAG = "tag";

    private static final String FILTER_TABLE_COLUMN_TAG = "filter";

    private static final String TG_CHAT_TABLE_COLUMN_CHAT_ID = "chat_id";
    private static final String TG_CHAT_TABLE_COLUMN_USER_ID = "user_id";

    private static final String LINK_METADATA_TABLE_COLUMN_LINK_ID = "link_id";
    private static final String LINK_METADATA_TABLE_COLUMN_SUBSCRIPTION_ID = "subscription_id";
    private static final String LINK_METADATA_TABLE_COLUMN_TAG_IDS = "tag_ids";
    private static final String LINK_METADATA_TABLE_COLUMN_TAG_NAMES = "tag_names";
    private static final String LINK_METADATA_TABLE_COLUMN_FILTER_IDS = "filter_ids";
    private static final String LINK_METADATA_TABLE_COLUMN_FILTER_NAMES = "filter_names";




    /**
     * Method of mapping the string received from the query to the Link entity.
     *
     * @param row    row set from the table.
     * @param rowNum number row from the received data from the query.
     * @return LinkEntity entity.
     */
    @SneakyThrows
    public static LinkDto mapRowToLink(ResultSet row, int rowNum) {
        return new LinkDto(
            row.getLong(COLUMN_ID),
            row.getString(LINK_TABLE_COLUMN_URI),
            row.getObject(LINK_TABLE_COLUMN_LAST_MODIFYING_DATE, OffsetDateTime.class),
            row.getObject(COLUMN_CREATE_AT, OffsetDateTime.class)
        );
    }

    @SneakyThrows
    public static TagDto mapRowToTag(ResultSet row, int rowNum) {
        return new TagDto(
            row.getLong(COLUMN_ID),
            row.getString(TAG_TABLE_COLUMN_TAG),
            row.getObject(COLUMN_CREATE_AT, OffsetDateTime.class)
        );
    }

    @SneakyThrows
    public static FilterDto mapRowToFilter(ResultSet row, int rowNum) {
        return new FilterDto(
            row.getLong(COLUMN_ID),
            row.getString(FILTER_TABLE_COLUMN_TAG),
            row.getObject(COLUMN_CREATE_AT, OffsetDateTime.class)
        );
    }
//
//    /**
//     * Method of mapping the string received from the query to the Relation entity.
//     *
//     * @param row    row set from the table.
//     * @param rowNum number row from the received data from the query.
//     * @return Relation entity.
//     */
//    @SneakyThrows
//    public static Subscriptions mapRowToRelation(ResultSet row, int rowNum) {
//        return new Subscriptions(
//            row.getLong(RELATION_TABLE_COLUMN_CHAT_ID),
//            row.getLong(RELATION_TABLE_COLUMN_LINK_ID),
//            row.getObject(RELATION_TABLE_COLUMN_CREATED_AT, OffsetDateTime.class),
//            row.getString(RELATION_TABLE_COLUMN_CREATED_BY)
//        );
//    }

    /**
     * Mapping method the term of the database query to the chat ID from the tgchat table.
     *
     * @param row    row set from the table.
     * @param rowNum number row from the received data from the query.
     * @return id chat.
     */
    @SneakyThrows
    public static Long mapRowToChatId(ResultSet row, int rowNum) {
        return row.getLong(COLUMN_ID);
    }

    /**
     * Mapping method the term of the database query to the entity TgChat from the tgchat table.
     *
     * @param row    row set from the table.
     * @param rowNum number row from the received data from the query.
     * @return TgChat entity from table tgchat.
     */
    @SneakyThrows
    public static UserDto mapRowToUserEntity(ResultSet row, int rowNum) {
        return new UserDto(
            row.getLong(COLUMN_ID),
            row.getLong(TG_CHAT_TABLE_COLUMN_CHAT_ID),
            row.getObject(COLUMN_CREATE_AT, OffsetDateTime.class)
        );
    }

    @SneakyThrows
    public static SubscriptionDto mapRowToSubscriptionEntity(ResultSet row, int rowNum) {
        return new SubscriptionDto(
            row.getLong(COLUMN_ID),
            row.getLong(TG_CHAT_TABLE_COLUMN_USER_ID),
            row.getLong(LINK_METADATA_TABLE_COLUMN_LINK_ID),
            row.getObject(COLUMN_CREATE_AT, OffsetDateTime.class)
        );
    }
}
