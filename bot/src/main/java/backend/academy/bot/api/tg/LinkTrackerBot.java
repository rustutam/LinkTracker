//package backend.academy.bot.api.tg;
//
//import backend.academy.bot.api.services.MessagesHandler;
//import com.pengrad.telegrambot.TelegramBot;
//import com.pengrad.telegrambot.UpdatesListener;
//import com.pengrad.telegrambot.model.BotCommand;
//import com.pengrad.telegrambot.model.Message;
//import com.pengrad.telegrambot.model.Update;
//import com.pengrad.telegrambot.request.SetMyCommands;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class LinkTrackerBot {
//    private final BotContext botContext;
//    private final Map<Long, Map<String, String>> userData;
//
//    public LinkTrackerBot(TelegramBot bot, MessagesHandler handler) {
//        botContext = new BotContext();
//        userData = new HashMap<>();
//        bot.setUpdatesListener(
//                updates -> {
//                    for (Update upd : updates) {
//                        Message message = upd.message();
//                        if (!userData.containsKey(message.chat().id())) {
//                            userData.put(message.chat().id(), new HashMap<>());
//                        }
//                        String command = getCommand(message.text());
//                        handler.handle(message, command, botContext, userData);
//                    }
//
//                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
//                },
//                e -> {
//                    if (e.response() != null) {
//                        log.error(
//                                "Error during updates listener: {} - {}",
//                                e.response().errorCode(),
//                                e.response().description());
//                    } else {
//                        log.error("Errors during updates listener: {}", Arrays.toString(e.getStackTrace()));
//                    }
//                });
//        bot.execute(new SetMyCommands(
//                new BotCommand(TgCommand.START.value(), "Register in bot"),
//                new BotCommand(TgCommand.HELP.value(), "Get help"),
//                new BotCommand(TgCommand.TRACK.value(), "Track link"),
//                new BotCommand(TgCommand.UNTRACK.value(), "Stop tracking link"),
//                new BotCommand(TgCommand.LIST.value(), "List tracked links")));
//    }
//
//    private String getCommand(String messageText) {
//        String possibleCommand = messageText.split(" ")[0];
//        if (possibleCommand.length() < 2) {
//            return "";
//        }
//        if (possibleCommand.charAt(0) == '/') {
//            return possibleCommand.substring(1);
//        }
//        return "";
//    }
//}
