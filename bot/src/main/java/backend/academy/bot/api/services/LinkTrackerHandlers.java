package backend.academy.bot.api.services;

import backend.academy.bot.api.services.commands.Command;
import backend.academy.bot.api.tg.FSM;
import com.pengrad.telegrambot.model.Message;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class LinkTrackerHandlers implements MessagesHandler {
    private final List<Command> handlers;

    public LinkTrackerHandlers(List<Command> handlers) {
        this.handlers = handlers.stream()
                .sorted(Comparator.comparingInt(Command::priority))
                .collect(Collectors.toList());
    }

    @Override
    public boolean handle(Message message, String command, FSM fsm, Map<Long, Map<String, String>> userData) {
        for (var handler : handlers) {
            if (handler.shouldBeExecuted(command, fsm)) {
                handler.execute(message, fsm, userData);
                return true;
            }
        }
        return false;
    }
}
