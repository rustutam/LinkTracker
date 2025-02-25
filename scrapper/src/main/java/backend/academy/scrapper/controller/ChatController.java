package backend.academy.scrapper.controller;

import backend.academy.scrapper.service.ScrapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ChatController {
    //    private final ChatService chatService;
    private final ScrapperService service;

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> tgChatIdPost(@PathVariable Long id) {
        service.register(id);
        // TODO возвращать не только ok
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> tgChatIdDelete(@PathVariable Long id) {
        service.unRegister(id);
        //TODO возвращать не только ok
        return ResponseEntity.ok().build();
    }
}
