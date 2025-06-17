package backend.academy.bot.api.services.commands;

import backend.academy.bot.api.cache.CacheStorage;
import backend.academy.bot.api.cache.KeyGenerator;
import backend.academy.bot.api.tg.TgCommand;
import backend.academy.bot.exceptions.ApiScrapperErrorResponseException;
import backend.academy.bot.models.Update;
import backend.academy.bot.sender.BotSender;
import backend.academy.bot.api.tg.BotContext;
import backend.academy.bot.api.tg.BotState;
import backend.academy.bot.sender.ScrapperSender;
import com.pengrad.telegrambot.model.request.ParseMode;
import java.util.Arrays;
import java.util.List;
import general.RegexCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static backend.academy.bot.utils.LogMessages.CHAT_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {
    public static String SKIP = "/skip";

    private final BotSender botSender;
    private final ScrapperSender scrapperSender;
    private final RegexCheck regexCheck;
    private final CacheStorage cache;
    private final KeyGenerator keyGenerator;


    @Override
    public void execute(Update update, BotContext context) {
        log.atInfo()
            .addKeyValue(CHAT_ID, update.chatId())
            .setMessage("Выполняется команда /track")
            .log();

        BotState currentBotState = context.botState();
        switch (currentBotState) {
            case AWAIT_COMMAND -> startHandle(update, context);
            case AWAIT_URL -> waitingForUrlHandle(update, context);
            case AWAIT_TAG -> waitingForTags(update, context);
            case AWAIT_FILTER -> waitingForFilters(update, context);
            default -> {
            }
        }

    }

    private void startHandle(Update update, BotContext context) {
        Long chatId = update.chatId();

        log.atInfo()
            .addKeyValue(CHAT_ID, chatId)
            .setMessage("Просят ввести url для начала отслеживания ссылки")
            .log();

        context.currentCommand(TgCommand.TRACK);
        context.botState(BotState.AWAIT_URL);
        botSender.sendMessage(chatId, "Введите URL для отслеживания или введите /stop чтобы выйти", ParseMode.Markdown);
    }

    private void waitingForUrlHandle(Update update, BotContext context) {
        Long chatId = update.chatId();
        String message = update.message();
        if (message.trim().equals("/stop")) {
            log.atInfo()
                .addKeyValue(CHAT_ID, chatId)
                .setMessage("Пользователь вышел из меню ввода ссылки для начала отслеживания")
                .log();
            context.reset();
            botSender.sendMessage(chatId, "Вы вышли из меню ввода ссылки", ParseMode.Markdown);
            return;
        }
        if (regexCheck.checkApi(message)) {
            context.url(update.message());
            context.botState(BotState.AWAIT_TAG);

            log.atInfo()
                .addKeyValue(CHAT_ID, chatId)
                .setMessage("Пользователя просят ввести теги для чата")
                .log();

            botSender.sendMessage(
                chatId,
                """
                    Введите теги, разделяя их пробелами (опционально).
                    Например: tag1 tag2 tag3.
                    Если теги не нужны - введите /skip
                    """,
                ParseMode.Markdown);
        } else {
            log.atInfo()
                .addKeyValue(CHAT_ID, chatId)
                .setMessage("Пользователь ввёл некорректную ссылку")
                .log();
            botSender.sendMessage(chatId, "Некорректно введена ссылка, введите заново, либо введите /stop", ParseMode.Markdown);
        }
    }

    private void waitingForTags(Update update, BotContext context) {
        Long chatId = update.chatId();
        String message = update.message();

        if (!message.equals(SKIP)) {
            log.atInfo()
                .addKeyValue(CHAT_ID, chatId)
                .setMessage("Пользователь ввёл теги")
                .log();
            List<String> tags = Arrays.asList(update.message().split(" "));
            context.tags(tags);
        } else {
            context.tags(List.of());
        }
        context.botState(BotState.AWAIT_FILTER);
        botSender.sendMessage(chatId, """
                Введите фильтры, разделяя их пробелами (опционально).
                Например: filter1 filter2 filter3.
                Если фильтры не нужны - введите /skip""",
            ParseMode.Markdown);
    }

    private void waitingForFilters(Update update, BotContext context) {
        Long chatId = update.chatId();
        String message = update.message();

        if (!message.equals(SKIP)) {
            log.atInfo()
                .addKeyValue(CHAT_ID, chatId)
                .setMessage("Пользователь ввёл фильтры")
                .log();
            List<String> filters = Arrays.asList(update.message().split(" "));
            context.filters(filters);
        } else {
            context.filters(List.of());
        }

        try {
            scrapperSender.subscribeToLink(chatId, context.url(), context.tags(), context.filters());
            cache.delete(keyGenerator.listCommand(chatId));
            botSender.sendMessage(
                chatId,
                new StringBuilder()
                    .append("Подписка на ссылку ")
                    .append(context.url())
                    .append(" успешно оформлена.\n")
                    .append("Теги: ")
                    .append(context.tags().isEmpty() ? "отсутствуют" : String.join(", ", context.tags()))
                    .append("\nФильтры: ")
                    .append(context.filters().isEmpty() ? "отсутствуют" : String.join(", ", context.filters()))
                    .toString(),
                ParseMode.Markdown);

        } catch (ApiScrapperErrorResponseException ex) {
            botSender.sendMessage(chatId, ex.description(), ParseMode.Markdown);
        }
        context.reset();
    }
}
