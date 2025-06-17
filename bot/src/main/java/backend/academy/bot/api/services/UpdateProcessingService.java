package backend.academy.bot.api.services;

import backend.academy.bot.api.services.commands.Command;
import backend.academy.bot.api.services.commands.TgCommandFactory;
import backend.academy.bot.api.tg.BotContext;
import backend.academy.bot.api.tg.BotState;
import backend.academy.bot.api.tg.TgCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UpdateProcessingService {
    private final TgCommandFactory tgCommandFactory;
    private final Map<Long, BotContext> contexts = new ConcurrentHashMap<>();

    public void processUpdate(Long chatId, String message) {
        contexts.putIfAbsent(chatId, new BotContext());
        BotContext context = contexts.get(chatId);
        backend.academy.bot.models.Update update = new backend.academy.bot.models.Update(chatId, message);

        if (context.botState() == BotState.AWAIT_COMMAND) {
            Command command = tgCommandFactory.getTgCommand(message);
            command.execute(update, context);
        } else {
            TgCommand currentCommand = context.currentCommand();
            Command command = tgCommandFactory.getTgCommand(currentCommand.value());
            command.execute(update, context);
        }




//        if (!(command instanceof UnknownCommand) && context.currentState() == State.DEFAULT) {
//            command.execute(update, context);
//        } else if (context.currentState() != State.DEFAULT) {
//            handler.handle(update, context);
//        } else {
//            sendMessage(chatId, "I didn't understand that. Use /help.");
//        }


    }

    private boolean isCommand(String messageText) {
        String possibleCommand = messageText.split(" ")[0];
        if (possibleCommand.length() >= 2) {
            return false;
        } else return possibleCommand.startsWith("/");
    }
}
