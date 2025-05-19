package backend.academy.bot.api.services.commands;

import backend.academy.bot.api.tg.FSM;
import com.pengrad.telegrambot.model.Message;
import java.util.Map;

public interface Command {
    int priority();

    boolean shouldBeExecuted(String command, FSM fsm);

    void execute(Message message, FSM fsm, Map<Long, Map<String, String>> userData);
}
