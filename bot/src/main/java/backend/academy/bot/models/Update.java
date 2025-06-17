package backend.academy.bot.models;

public record Update(
    Long chatId,
    String message
) {
}
