package backend.academy.bot.api.services.commands;

import backend.academy.bot.api.cache.CacheStorage;
import backend.academy.bot.api.cache.KeyGenerator;
import backend.academy.bot.api.services.scrapper.ApiScrapper;
import backend.academy.bot.sender.BotSender;
import backend.academy.bot.api.tg.FSM;
import backend.academy.bot.api.tg.State;
import backend.academy.bot.exceptions.ApiScrapperErrorResponseException;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import java.util.Arrays;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnterFiltersCommand implements Command {
    private final ApiScrapper scrapper;
    private final BotSender messager;
    private final CacheStorage cache;
    private final KeyGenerator keyGenerator;

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean shouldBeExecuted(String command, FSM fsm) {
        return fsm.getCurrentState() == State.EnterFilters;
    }

    @Override
    public void execute(Message message, FSM fsm, Map<Long, Map<String, String>> userData) {
        var chatId = message.chat().id();
        userData.get(chatId).put("filters", message.text());
        messager.sendMessage(
                message.chat().id(),
                "Started tracking " + userData.get(chatId).get("url") + "\n with tags "
                        + userData.get(chatId).get("tags") + "\n with filters "
                        + userData.get(chatId).get("filters"),
                ParseMode.Markdown);
        try {
            scrapper.subscribeToLink(
                    message.chat().id(),
                    userData.get(chatId).get("url"),
                    Arrays.stream(userData.get(chatId).get("tags").split(" ")).toList(),
                    Arrays.stream(userData.get(chatId).get("filters").split(" "))
                            .toList());
        } catch (ApiScrapperErrorResponseException ex) {
            messager.sendMessage(
                    message.chat().id(), "_An error occured during request!_\n" + ex.description(), ParseMode.Markdown);
        }
        cache.delete(keyGenerator.listCommand(message));
        fsm.setCurrentState(State.None);
    }
}
