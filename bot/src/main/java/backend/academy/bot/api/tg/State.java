package backend.academy.bot.api.tg;

public enum State {
    None,
    EnterTags,
    EnterFilters,

    START,
    WAITING_FOR_URL,
    WAITING_FOR_TAGS,
    WAITING_FOR_FILTERS,
}
