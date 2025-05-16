package backend.academy.bot.api.services.commands;

import backend.academy.bot.api.tg.BotMessager;
import backend.academy.bot.api.tg.FSM;
import backend.academy.bot.api.tg.States;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnterTagsCommand implements Command {
    private final BotMessager messager;

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean shouldBeExecuted(String command, FSM fsm) {
        return fsm.getCurrentState() == States.EnterTags;
    }

    @Override
    public void execute(Message message, FSM fsm, Map<Long, Map<String, String>> userData) {
        var chatId = message.chat().id();
        userData.get(chatId).put("tags", message.text());
        messager.sendMessage(message.chat().id(), "Enter filters:", ParseMode.Markdown);
        fsm.setCurrentState(States.EnterFilters);
    }
}
