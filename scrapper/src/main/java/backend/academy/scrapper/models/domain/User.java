package backend.academy.scrapper.models.domain;

import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.UserId;

public record User(
    UserId userId,
    ChatId chatId
) {
}
