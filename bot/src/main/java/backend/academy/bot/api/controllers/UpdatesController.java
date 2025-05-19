package backend.academy.bot.api.controllers;

import backend.academy.bot.api.dto.LinkUpdate;
import backend.academy.bot.api.services.UpdatesService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UpdatesController {
    private final UpdatesService updatesService;

    @RateLimiter(name = "rateLimiter")
    @PostMapping("/updates")
    public ResponseEntity<Void> handleUpdates(@RequestBody LinkUpdate update) {
        updatesService.notifySubscribers(update);
        return ResponseEntity.ok().build();
    }
}
