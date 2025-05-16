package backend.academy.bot.api.tg;

import backend.academy.bot.api.services.MessagesHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LinkTrackerBot {
    private final FSM fsm;
    private final Map<Long, Map<String, String>> userData;

    public LinkTrackerBot(TelegramBot bot, MessagesHandler handler) {
        fsm = new FSM();
        userData = new HashMap<>();
        bot.setUpdatesListener(
                updates -> {
                    for (Update upd : updates) {
                        Message message = upd.message();
                        if (!userData.containsKey(message.chat().id())) {
                            userData.put(message.chat().id(), new HashMap<>());
                        }
                        String command = getCommand(message.text());
                        handler.handle(message, command, fsm, userData);
                    }

                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                },
                e -> {
                    if (e.response() != null) {
                        log.error(
                                "Error during updates listener: {} - {}",
                                e.response().errorCode(),
                                e.response().description());
                    } else {
                        log.error("Errors during updates listener: {}", Arrays.toString(e.getStackTrace()));
                    }
                });
        bot.execute(new SetMyCommands(
                new BotCommand(TgCommand.START.cmdName(), "Register in bot"),
                new BotCommand(TgCommand.HELP.cmdName(), "Get help"),
                new BotCommand(TgCommand.TRACK.cmdName(), "Track link"),
                new BotCommand(TgCommand.UNTRACK.cmdName(), "Stop tracking link"),
                new BotCommand(TgCommand.LIST.cmdName(), "List tracked links")));
    }

    private String getCommand(String messageText) {
        String possibleCommand = messageText.split(" ")[0];
        if (possibleCommand.length() < 2) {
            return "";
        }
        if (possibleCommand.charAt(0) == '/') {
            return possibleCommand.substring(1);
        }
        return "";
    }
}
