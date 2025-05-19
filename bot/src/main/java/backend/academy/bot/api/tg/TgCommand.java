package backend.academy.bot.api.tg;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TgCommand {
    START("start"),
    HELP("help"),
    TRACK("track"),
    UNTRACK("untrack"),
    LIST("list");

    private final String cmdName;
}
