package backend.academy.bot.api.services.commands;

import backend.academy.bot.api.tg.BotSender;
import backend.academy.bot.api.tg.FSM;
import backend.academy.bot.api.tg.States;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnknownTextOrCommand implements Command {
    private final BotSender messager;

    @Override
    public int priority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean shouldBeExecuted(String command, FSM fsm) {
        return true;
    }

    @Override
    public void execute(Message message, FSM fsm, Map<Long, Map<String, String>> userData) {
        messager.sendMessage(message.chat().id(), "_I do not know this command!_", ParseMode.Markdown);
        fsm.setCurrentState(States.None);
    }
}
