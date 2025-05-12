package backend.academy.bot.api.services;

import backend.academy.bot.api.tg.FSM;
import com.pengrad.telegrambot.model.Message;
import java.util.Map;

public interface MessagesHandler {

    boolean handle(Message message, String command, FSM fsm, Map<Long, Map<String, String>> userData);
}
