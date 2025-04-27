package backend.academy.scrapper.models.domain;

import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.UserId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.time.OffsetDateTime;


@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class User {
    private UserId userId;
    private ChatId chatId;
    private OffsetDateTime createdAt;
//TODO убрать коментарии

//    public static User of(ChatId chatId) {
//        return builder()
//            .userId(new UserId(0L))
//            .chatId(chatId)
//            .build();
//    }
}
