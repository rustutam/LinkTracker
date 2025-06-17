package backend.academy.scrapper.controller;

import backend.academy.scrapper.handler.ChatHandler;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatHandler chatHandler;

    @RateLimiter(name = "rateLimiter")
    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> tgChatIdPost(@PathVariable Long id) {
        chatHandler.register(id);
        return ResponseEntity.ok().build();
    }

    @RateLimiter(name = "rateLimiter")
    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> tgChatIdDelete(@PathVariable Long id) {
        chatHandler.unregister(id);
        return ResponseEntity.ok().build();
    }
}
