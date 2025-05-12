package backend.academy.bot.api.controllers;

import backend.academy.bot.api.dto.LinkUpdate;
import backend.academy.bot.api.services.UpdatesService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@ConditionalOnProperty(name = "app.message-transport", havingValue = "HTTP")
public class UpdatesController {

    private final UpdatesService updatesService;

    public UpdatesController(UpdatesService service) {
        updatesService = service;
    }

    @PostMapping
    public ResponseEntity<?> handleUpdates(@RequestBody LinkUpdate update) {
        updatesService.notifySubscribers(update);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
