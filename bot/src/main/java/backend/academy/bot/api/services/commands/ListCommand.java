package backend.academy.bot.api.services.commands;

import backend.academy.bot.api.cache.CacheStorage;
import backend.academy.bot.api.cache.KeyGenerator;
import backend.academy.bot.api.dto.ListLinksItem;
import backend.academy.bot.api.dto.ListLinksResponse;
import backend.academy.bot.api.tg.BotContext;
import backend.academy.bot.exceptions.ApiScrapperErrorResponseException;
import backend.academy.bot.models.Update;
import backend.academy.bot.sender.BotSender;
import backend.academy.bot.sender.ScrapperSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.request.ParseMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static backend.academy.bot.utils.LogMessages.CHAT_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    private final ScrapperSender scrapperSender;
    private final BotSender botSender;
    private final CacheStorage cache;
    private final KeyGenerator keyGenerator;

    @Override
    public void execute(Update update, BotContext botContext) {
        Long chatId = update.chatId();

        log.atInfo()
            .addKeyValue(CHAT_ID, chatId)
            .setMessage("Выполняется команда /list")
            .log();

        ListLinksResponse response = null;
        ObjectMapper objectMapper = new ObjectMapper();
        String key = keyGenerator.listCommand(chatId);
        if (cache.hasKey(key)) {
            try {
                response = objectMapper.readValue(cache.get(key), ListLinksResponse.class);
            } catch (Exception ignored) {
                cache.delete(key);
            }
        }
        if (response == null) {
            try {
                response = scrapperSender.getLinks(chatId);
                cache.store(key, objectMapper.writeValueAsString(response));
            } catch (ApiScrapperErrorResponseException ex) {
                botSender.sendMessage(
                    chatId,
                    ex.description(),
                    ParseMode.Markdown);
                return;
            } catch (JsonProcessingException ex) {
                botSender.sendMessage(
                    chatId,
                    "Ошибка получения списка подписок\n" + ex.getMessage(),
                    ParseMode.Markdown);
                return;
            }
        }

        StringBuilder msg = new StringBuilder();
        if (response.links().isEmpty()) {
            msg.append("Список отслеживаемых ссылок пуст.");
        }
        for (ListLinksItem link : response.links()) {
            msg
                .append(link.url())
                .append("\n")
                .append("Теги: ")
                .append(link.tags().isEmpty() ? "отсутствуют" : String.join(", ", link.tags()))
                .append("\nФильтры: ")
                .append(link.filters().isEmpty() ? "отсутствуют" : String.join(", ", link.filters()))
                .append("\n\n");

        }
        botSender.sendMessage(chatId, msg.toString(), ParseMode.Markdown);
    }
}
