package backend.academy.bot.api.tg;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;

@Component
public class TgBotMessager implements BotMessager {
    private final TelegramBot bot;

    public TgBotMessager(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean sendMessage(Long chatId, String message, ParseMode mode) {
        SendMessage request = new SendMessage(chatId, message).parseMode(mode);
        SendResponse response = bot.execute(request);
        return response.isOk();
    }
}
