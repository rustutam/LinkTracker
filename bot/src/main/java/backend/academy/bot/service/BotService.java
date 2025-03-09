package backend.academy.bot.service;

import backend.academy.bot.commands.CommandContainer;
import backend.academy.bot.commands.State;
import backend.academy.bot.commands.TrackCommand;
import backend.academy.bot.commands.UntrackCommand;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("ReturnCount")
public class BotService {

    private final TelegramBot bot;
    private final CommandContainer commandContainer;
    private final TrackCommand trackCommand;
    private final UntrackCommand untrackCommand;

    @PostConstruct
    public void startListening() {
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message().text() != null) {
                    log.atInfo()
                            .addKeyValue("chatId", update.message().chat().id())
                            .addKeyValue("userMessage", update.message().text())
                            .setMessage("Пришло сообщение")
                            .log();
                    handleMessage(update);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public void handleMessage(Update update) {
        Long chatId = update.message().chat().id();
        String messageText = update.message().text();

        State currentTrackState = trackCommand.userStates().getOrDefault(chatId, State.START);
        State currentUntrackState = untrackCommand.userStates().getOrDefault(chatId, State.START);

        if (currentTrackState != State.START) {
            trackCommand.execute(chatId, messageText);
            return;
        }

        if (currentUntrackState != State.START) {
            untrackCommand.execute(chatId, messageText);
            return;
        }

        if (messageText != null) {
            commandContainer.retrieveCommand(messageText.trim()).execute(chatId, messageText);
        }
    }
}
