package backend.academy.bot.api.services.commands;

import backend.academy.bot.api.cache.CacheStorage;
import backend.academy.bot.api.cache.KeyGenerator;
import backend.academy.bot.api.dto.ListLinksItem;
import backend.academy.bot.api.dto.ListLinksResponse;
import backend.academy.bot.api.services.scrapper.ApiScrapper;
import backend.academy.bot.sender.BotSender;
import backend.academy.bot.api.tg.FSM;
import backend.academy.bot.api.tg.State;
import backend.academy.bot.api.tg.TgCommand;
import backend.academy.bot.exceptions.ApiScrapperErrorResponseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
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
        return Objects.equals(command, TgCommand.LIST.cmdName());
    }

    @Override
    public void execute(Message message, FSM fsm, Map<Long, Map<String, String>> userData) {
        fsm.setCurrentState(State.None);

        ListLinksResponse response = null;
        ObjectMapper objectMapper = new ObjectMapper();
        String key = keyGenerator.listCommand(message);
        if (cache.hasKey(key)) {
            try {
                response = objectMapper.readValue(cache.get(key), ListLinksResponse.class);
            } catch (Exception ignored) {
                cache.delete(key);
            }
        }
        if (response == null) {
            try {
                response = scrapper.getLinks(message.chat().id());
                cache.store(key, objectMapper.writeValueAsString(response));
            } catch (ApiScrapperErrorResponseException ex) {
                messager.sendMessage(
                        message.chat().id(),
                        "_An error occured during request!_\n" + ex.description(),
                        ParseMode.Markdown);
                return;
            } catch (JsonProcessingException ex) {
                messager.sendMessage(
                        message.chat().id(),
                        "_An error occured during request!_\n" + ex.getMessage(),
                        ParseMode.Markdown);
                return;
            }
        }

        StringBuilder msg = new StringBuilder();
        if (response.links().isEmpty()) {
            msg.append("You didnt subscribe to any link.");
        }
        for (ListLinksItem i : response.links()) {
            msg.append(i.url())
                    .append(" | ")
                    .append(i.filters())
                    .append(" | ")
                    .append(i.tags())
                    .append("\n");
        }
        messager.sendMessage(message.chat().id(), msg.toString(), ParseMode.Markdown);
    }
}
