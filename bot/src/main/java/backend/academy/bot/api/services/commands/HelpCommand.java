package backend.academy.bot.api.services.commands;

import backend.academy.bot.api.tg.BotMessager;
import backend.academy.bot.api.tg.FSM;
import backend.academy.bot.api.tg.States;
import backend.academy.bot.api.tg.TgCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {
    private final BotMessager messager;

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean shouldBeExecuted(String command, FSM fsm) {
        return Objects.equals(command, TgCommand.HELP.cmdName());
    }

    @Override
    public void execute(Message message, FSM fsm, Map<Long, Map<String, String>> userData) {
        messager.sendMessage(message.chat().id(), "_This is help command!_", ParseMode.Markdown);
        fsm.setCurrentState(States.None);
    }
}
