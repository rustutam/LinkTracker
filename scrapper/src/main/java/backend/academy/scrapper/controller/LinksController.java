package backend.academy.scrapper.controller;

import backend.academy.scrapper.models.api.request.AddLinkRequest;
import backend.academy.scrapper.models.api.request.RemoveLinkRequest;
import backend.academy.scrapper.models.api.response.LinkResponse;
import backend.academy.scrapper.models.api.response.ListLinksResponse;
import backend.academy.scrapper.service.ScrapperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LinksController {
    //    private final LinkService linkService;
    private final ScrapperService service;

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> linksGet(
            @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId
    ) {
        val listLinksResponse = service.getListLinks(tgChatId);

        //TODO: возвращать другие ответы при ошибках
        return ResponseEntity
                .ok()
                .body(listLinksResponse);
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> linksPost(
            @RequestHeader(value = "Tg-Chat-Id", required = true)
            Long tgChatId,

            @Valid
            @RequestBody
            AddLinkRequest addLinkRequest
    ) {
        val linkResponse = service.addLink(tgChatId, addLinkRequest.link());
        return ResponseEntity
                .ok()
                .body(linkResponse);
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> linksDelete(
            @RequestHeader(value = "Tg-Chat-Id", required = true)
            Long tgChatId,

            @Valid
            @RequestBody
            RemoveLinkRequest removeLinkRequest) {
        val linkResponse = service.removeLink(tgChatId, removeLinkRequest.link());
        return ResponseEntity
                .ok()
                .body(linkResponse);

    }
}
