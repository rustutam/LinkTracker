package backend.academy.bot.api.tg;

import com.pengrad.telegrambot.model.request.ParseMode;

public interface BotSender {
    boolean sendMessage(Long chatId, String message, ParseMode parseMode);
}
