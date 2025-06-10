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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static backend.academy.bot.utils.LogMessages.CHAT_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {
    private final BotSender botSender;
    private final CacheStorage cache;
    private final KeyGenerator keyGenerator;
    private final ScrapperSender scrapperSender;

    @Override
    public void execute(Update update, BotContext botContext) {
        Long chatId = update.chatId();

        log.atInfo()
            .addKeyValue(CHAT_ID, chatId)
            .setMessage("Выполняется команда /untrack")
            .log();

        BotState currentBotState = botContext.botState();
        switch (currentBotState) {
            case AWAIT_COMMAND -> startHandle(update, botContext);
            case AWAIT_URL -> waitingForUrlHandle(update, botContext);
            default -> {
            }
        }
    }

    private void startHandle(Update update, BotContext botContext) {
        Long chatId = update.chatId();

        log.atInfo()
            .addKeyValue(CHAT_ID, chatId)
            .setMessage("Пользователя просят ввести url для прекращения отслеживания")
            .log();

        botContext.currentCommand(TgCommand.UNTRACK);
        botContext.botState(BotState.AWAIT_URL);
        botSender.sendMessage(chatId, "Введите url для прекращения отслеживания или введите /stop чтобы выйти", ParseMode.Markdown);
    }

    private void waitingForUrlHandle(Update update, BotContext context) {
        Long chatId = update.chatId();
        String message = update.message();
        if (message.trim().equals("/stop")) {
            context.reset();
            botSender.sendMessage(chatId, "Вы вышли из меню ввода ссылки", ParseMode.Markdown);
            log.atInfo()
                .addKeyValue(CHAT_ID, chatId)
                .setMessage("Пользователь вышел из меню ввода ссылки для прекращения отслеживания")
                .log();
            return;
        }

        try {
            scrapperSender.unSubscribeToLink(chatId, message);

            botSender.sendMessage(chatId, "Отслеживание ссылки прекращено", ParseMode.Markdown);
            cache.delete(keyGenerator.listCommand(chatId));
            context.reset();
            log.atInfo()
                .addKeyValue(CHAT_ID, chatId)
                .addKeyValue("userMessage", message)
                .setMessage("Отслеживание ссылки прекращено")
                .log();
        } catch (ApiScrapperErrorResponseException ex) {
            botSender.sendMessage(chatId, ex.description() + " Введите заново, либо введите /stop", ParseMode.Markdown);
        }
    }
}
