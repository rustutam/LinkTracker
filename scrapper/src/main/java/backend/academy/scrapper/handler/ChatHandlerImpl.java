package backend.academy.scrapper.handler;

import backend.academy.scrapper.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatHandlerImpl implements ChatHandler {
    private final ChatService chatService;

    @Override
    public void register(long chatId) {
        chatService.register(chatId);
    }

    @Override
    public void unregister(long chatId) {
        chatService.unRegister(chatId);
    }
}
