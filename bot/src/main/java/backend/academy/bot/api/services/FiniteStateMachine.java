//package backend.academy.bot.api.services;
//
//import backend.academy.bot.api.services.commands.Command;
//import backend.academy.bot.api.services.commands.HelpCommand;
//import backend.academy.bot.api.services.commands.ListCommand;
//import backend.academy.bot.api.services.commands.StartCommand;
//import backend.academy.bot.api.services.commands.TrackCommand;
//import backend.academy.bot.api.services.commands.UnknownCommand;
//import backend.academy.bot.api.services.commands.UntrackCommand;
//import backend.academy.bot.api.tg.BotContext;
//import backend.academy.bot.api.tg.BotState;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import java.util.List;
//import java.util.Map;
//import static backend.academy.bot.api.tg.BotState.AWAIT_COMMAND;
//
//@Component
//@RequiredArgsConstructor
//public class FiniteStateMachine {
//    private final StartCommand startCommand;
//    private final HelpCommand helpCommand;
//    private final ListCommand listCommand;
//    private final TrackCommand trackCommand;
//    private final UntrackCommand untrackCommand;
//    private final UnknownCommand unknownCommand;
//    private final Map<String, BotState> stateTransitions = Map.of(
//        "/start", AWAIT_COMMAND,
//        "/help", AWAIT_COMMAND,
//        "/track", BotState.AWAIT_URL,
//        "/untrack", BotState.AWAIT_URL,
//        "/list", AWAIT_COMMAND
//    );
//    private final List<String> basicCommands = List.of("/start", "/help", "/track", "/untrack", "/list");
//
//    public Command resolveState(String inputMessage, BotContext botContext) {
//        BotState currentState = botContext.botState();
//
//        if (currentState == AWAIT_COMMAND) {
//            switch (inputMessage) {
//                case "/start", "/help", "/list":
//                    botContext.botState(AWAIT_COMMAND);
//                    break;
//                case "/track", "/untrack":
//                    botContext.botState(BotState.AWAIT_URL);
//                    break;
//            }
//            return resolveCommandState(inputMessage);
//        }
//        if (currentState == BotState.AWAIT_URL) {
//            if (inputMessage.equals("/stop")) {
//                botContext.botState(AWAIT_COMMAND);
//                return stopCommand;
//            }
//            botContext.botState(AWAIT_COMMAND);
//            return urlCommand;
//        }
//        if (currentState == BotState.AWAIT_TAG) {
//            return tagsCommand;
//        }
//        if (currentState == BotState.AWAIT_FILTER) {
//            return tagsCommand;
//        }
//    }
//
//    private Command resolveCommandState(String inputMessage){
//        switch (inputMessage) {
//            case "/start" -> startCommand;
//            case "/help" -> helpCommand;
//            case "/list" -> listCommand;
//            case "/track" -> trackCommand;
//            case "/untrack" -> untrackCommand;
//            default -> unknownCommand;
//        }
//    }
//}
