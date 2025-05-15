package backend.academy.scrapper.handler;

import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatHandlerImpl implements ChatHandler {
    private final ChatService chatService;

    @Override
    public void register(long id) {
        chatService.register(new ChatId(id));
    }

    @Override
    public void unregister(long id) {
        chatService.unRegister(new ChatId(id));
    }
}
