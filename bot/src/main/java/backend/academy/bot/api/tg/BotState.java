package backend.academy.bot.api.tg;

public enum BotState {
    AWAIT_COMMAND, // состояние ожидания команды
    AWAIT_URL,    // состояние ожидания URL
    AWAIT_TAG,    // состояние ожидания тегов
    AWAIT_FILTER, // состояние ожидания фильтров
    UNKNOWN_COMMAND
}
