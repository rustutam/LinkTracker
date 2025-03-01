package backend.academy.scrapper.controller;

import backend.academy.scrapper.handler.ChatHandler;
import backend.academy.scrapper.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Handler;

@RestController
@RequiredArgsConstructor
public class ChatController implements Controller{
    private final ChatHandler chatHandler;

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> tgChatIdPost(@PathVariable Long id) {
        chatHandler.register(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> tgChatIdDelete(@PathVariable Long id) {
        chatHandler.unregister(id);
        return ResponseEntity.ok().build();
    }
}
