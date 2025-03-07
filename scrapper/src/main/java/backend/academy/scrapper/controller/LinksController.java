package backend.academy.scrapper.controller;

import backend.academy.scrapper.handler.LinkHandler;
import backend.academy.scrapper.models.api.request.AddLinkRequest;
import backend.academy.scrapper.models.api.request.RemoveLinkRequest;
import backend.academy.scrapper.models.api.response.LinkResponse;
import backend.academy.scrapper.models.api.response.ListLinksResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LinksController implements Controller {
    private final LinkHandler linkHandler;

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getLinks(
            @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId
    ) {
        ListLinksResponse listLinksResponse = linkHandler.getLinks(tgChatId);

        return ResponseEntity
                .ok()
                .body(listLinksResponse);
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLinks(
            @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId,
            @Valid @RequestBody AddLinkRequest addLinkRequest
    ) {
        log.info("Добавление ссылки{}",addLinkRequest.link());
        LinkResponse linkResponse = linkHandler.addLink(tgChatId, addLinkRequest);
        return ResponseEntity
                .ok()
                .body(linkResponse);
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> deleteLinks(
            @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId,
            @Valid @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        LinkResponse linkResponse = linkHandler.removeLink(tgChatId, removeLinkRequest);
        return ResponseEntity
                .ok()
                .body(linkResponse);

    }
}
