package backend.academy.bot.api.tg;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BotContext {
    private TgCommand currentCommand = TgCommand.START;
    private BotState botState = BotState.AWAIT_COMMAND;
    private String url;
    private List<String> tags;
    private List<String> filters;


    public void reset() {
        currentCommand = TgCommand.START;
        botState = BotState.AWAIT_COMMAND;
        url = null;
        tags = List.of();
        filters = List.of();
    }
}
