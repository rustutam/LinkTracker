package backend.academy.bot.api.services.commands;

import backend.academy.bot.api.tg.BotContext;
import backend.academy.bot.models.Update;
import backend.academy.bot.sender.BotSender;
import com.pengrad.telegrambot.model.request.ParseMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SkipCommand implements Command {
    private final BotSender botSender;

    @Override
    public void execute(Update update, BotContext context) {
        Long chatId = update.chatId();
        context.reset();
        botSender.sendMessage(chatId, "Операция отменена. Мы можете вводить другую команду", ParseMode.Markdown);
    }
}
