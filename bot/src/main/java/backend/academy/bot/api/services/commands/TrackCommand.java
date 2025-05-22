package backend.academy.bot.api.services.commands;

import backend.academy.bot.sender.BotSender;
import backend.academy.bot.api.tg.FSM;
import backend.academy.bot.api.tg.State;
import backend.academy.bot.api.tg.TgCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private final BotSender messager;

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean shouldBeExecuted(String command, FSM fsm) {
        return Objects.equals(command, TgCommand.TRACK.cmdName());
    }

    @Override
    public void execute(Message message, FSM fsm, Map<Long, Map<String, String>> userData) {
        var chatId = message.chat().id();
        userData.put(chatId, new HashMap<>());
        String[] args = message.text().split(" ");
        if (args.length != 2) {
            messager.sendMessage(message.chat().id(), "Args:\n/track <url>", ParseMode.Markdown);
            return;
        }
        messager.sendMessage(message.chat().id(), "Enter tags:", ParseMode.Markdown);
        userData.get(chatId).put("url", args[1]);
        fsm.setCurrentState(State.EnterTags);
    }
}
