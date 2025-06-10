package backend.academy.bot.api.services.commands;

import backend.academy.bot.api.tg.BotContext;
import backend.academy.bot.exceptions.ApiScrapperErrorResponseException;
import backend.academy.bot.models.Update;
import backend.academy.bot.sender.BotSender;
import backend.academy.bot.sender.ScrapperSender;
import com.pengrad.telegrambot.model.request.ParseMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static backend.academy.bot.utils.BotMessages.CHAT_REGISTERED;
import static backend.academy.bot.utils.LogMessages.CHAT_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommand implements Command {
    private final ScrapperSender scrapperSender;
    private final BotSender botSender;

    @Override
    public void execute(Update update, BotContext context) {
        Long chatId = update.chatId();

        log.atInfo()
            .addKeyValue(CHAT_ID, chatId)
            .setMessage("Выполняется команда /start")
            .log();

        try {
            scrapperSender.registerChat(chatId);
            botSender.sendMessage(chatId, CHAT_REGISTERED, ParseMode.Markdown);
            log.atInfo()
                .addKeyValue(CHAT_ID, chatId)
                .setMessage(CHAT_REGISTERED)
                .log();
        } catch (ApiScrapperErrorResponseException ex) {
            botSender.sendMessage(chatId, ex.description(), ParseMode.Markdown);
        }
    }
}
