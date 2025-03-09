package backend.academy.bot.controller;

import backend.academy.bot.service.UpdateService;
import dto.LinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateController {
    private final UpdateService updateService;

    @PostMapping("/updates")
    public String update(@RequestBody LinkUpdate linkUpdate) {
        updateService.update(linkUpdate.tgChatIds(), linkUpdate.url(), linkUpdate.description());
        return "Обновление обработано";
    }
}
