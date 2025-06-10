package backend.academy.bot.api.services.commands;

import backend.academy.bot.models.Update;
import backend.academy.bot.sender.BotSender;
import backend.academy.bot.api.tg.BotContext;
import com.pengrad.telegrambot.model.request.ParseMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnknownCommand implements Command {
    private final BotSender botSender;

    @Override
    public void execute(Update update, BotContext botContext) {
        Long chatId = update.chatId();
        botSender.sendMessage(chatId, "Неизвестная команда. Для справки введите /help", ParseMode.Markdown);
    }
}
