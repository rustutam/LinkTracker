package backend.academy.bot.api.services.commands;

import backend.academy.bot.models.Update;
import backend.academy.bot.sender.BotSender;
import backend.academy.bot.api.tg.BotContext;
import backend.academy.bot.utils.BotMessages;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static backend.academy.bot.utils.LogMessages.CHAT_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {
    private final BotSender sender;

    @Override
    public void execute(Update update, BotContext context) {
        Long chatId = update.chatId();

        log.atInfo()
            .addKeyValue(CHAT_ID, chatId)
            .setMessage("Выполняется команда /help")
            .log();
        sender.sendMessage(chatId, BotMessages.HELP_MESSAGE, ParseMode.Markdown);
    }
}
