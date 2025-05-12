package backend.academy.bot.api.services.scrapper;

import backend.academy.bot.api.dto.ApiErrorResponse;
import backend.academy.bot.api.dto.LinkResponse;
import backend.academy.bot.api.dto.ListLinksResponse;
import java.util.List;

public interface ApiScrapper {

    void registerChat(Long chatId) throws ApiErrorResponse;

    void deleteChat(Long chatId) throws ApiErrorResponse;

    ListLinksResponse getLinks(Long chatId) throws ApiErrorResponse;

    LinkResponse subscribeToLink(Long chatId, String link, List<String> tags, List<String> filters)
            throws ApiErrorResponse;

    LinkResponse unSubscribeToLink(Long chatId, String link) throws ApiErrorResponse;
}
