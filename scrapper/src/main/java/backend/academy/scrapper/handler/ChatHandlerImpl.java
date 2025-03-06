package backend.academy.scrapper.handler;

import backend.academy.scrapper.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatHandlerImpl implements ChatHandler {
    private final ChatService chatService;

    public void register(long chatId) {
        chatService.register(chatId);
    }

    public void unregister(long chatId) {
        chatService.unRegister(chatId);
    }



}
