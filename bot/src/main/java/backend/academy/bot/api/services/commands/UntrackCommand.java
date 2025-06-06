package backend.academy.bot.api.services.commands;

import backend.academy.bot.api.cache.CacheStorage;
import backend.academy.bot.api.cache.KeyGenerator;
import backend.academy.bot.api.dto.ApiErrorResponse;
import backend.academy.bot.api.services.scrapper.ApiScrapper;
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
public class UntrackCommand implements Command {
    private final ApiScrapper scrapper;
    private final BotMessager messager;
    private final CacheStorage cache;
    private final KeyGenerator keyGenerator;

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean shouldBeExecuted(String command, FSM fsm) {
        return Objects.equals(command, TgCommand.UNTRACK.cmdName());
    }

    @Override
    public void execute(Message message, FSM fsm, Map<Long, Map<String, String>> userData) {
        String[] args = message.text().split(" ");
        if (args.length != 2) {
            messager.sendMessage(message.chat().id(), "Args:\n/untrack <url>", ParseMode.Markdown);
            return;
        }
        try {
            scrapper.unSubscribeToLink(message.chat().id(), args[1]);
        } catch (ApiErrorResponse ex) {
            messager.sendMessage(
                    message.chat().id(), "_An error occured during request!_\n" + ex.description(), ParseMode.Markdown);
        }
        cache.delete(keyGenerator.listCommand(message));
        fsm.setCurrentState(States.None);
    }
}
