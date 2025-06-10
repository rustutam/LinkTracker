package backend.academy.bot.api.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramUpdatesListenerService {
    private final TelegramBot bot;
    private final UpdateProcessingService updateProcessingService;

    @PostConstruct
    public void startListening() {
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message().text() != null) {
                    Long chatId = update.message().chat().id();
                    String messageText = update.message().text();
                    log.atInfo()
                            .addKeyValue("chatId", chatId)
                            .addKeyValue("userMessage", messageText)
                            .setMessage("Пришло сообщение")
                            .log();
                    updateProcessingService.processUpdate(chatId, messageText);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
