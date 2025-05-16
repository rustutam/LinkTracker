package backend.academy.bot.api.cache;

import com.pengrad.telegrambot.model.Message;
import org.springframework.stereotype.Component;

@Component
public class KeyGenerator {
    public String listCommand(Message message) {
        return "/list/" + message.chat().id();
    }
}
