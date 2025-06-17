package backend.academy.scrapper.controller;

import backend.academy.scrapper.handler.LinkHandler;
import dto.request.AddLinkRequest;
import dto.request.RemoveLinkRequest;
import dto.response.LinkResponse;
import dto.response.ListLinksResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LinksController {
    private final LinkHandler linkHandler;

    @RateLimiter(name = "rateLimiter")
    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getLinks(
            @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId) {
        ListLinksResponse listLinksResponse = linkHandler.getLinks(tgChatId);

        return ResponseEntity.ok().body(listLinksResponse);
    }

    @RateLimiter(name = "rateLimiter")
    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLinks(
            @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId,
            @Valid @RequestBody AddLinkRequest addLinkRequest) {
        log.info("Добавление ссылки{}", addLinkRequest.link());
        LinkResponse linkResponse = linkHandler.addLink(tgChatId, addLinkRequest);
        return ResponseEntity.ok().body(linkResponse);
    }

    @RateLimiter(name = "rateLimiter")
    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> deleteLinks(
            @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId,
            @Valid @RequestBody RemoveLinkRequest removeLinkRequest) {
        LinkResponse linkResponse = linkHandler.removeLink(tgChatId, removeLinkRequest);
        return ResponseEntity.ok().body(linkResponse);
    }
}
