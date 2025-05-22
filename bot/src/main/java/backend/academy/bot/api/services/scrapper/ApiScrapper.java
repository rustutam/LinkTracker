package backend.academy.bot.api.services.scrapper;

import backend.academy.bot.api.dto.LinkResponse;
import backend.academy.bot.api.dto.ListLinksResponse;
import backend.academy.bot.exceptions.ApiScrapperErrorResponseException;
import java.util.List;

public interface ApiScrapper {

    void registerChat(Long chatId) throws ApiScrapperErrorResponseException;

    void deleteChat(Long chatId) throws ApiScrapperErrorResponseException;

    ListLinksResponse getLinks(Long chatId) throws ApiScrapperErrorResponseException;

    LinkResponse subscribeToLink(Long chatId, String link, List<String> tags, List<String> filters)
            throws ApiScrapperErrorResponseException;

    LinkResponse unSubscribeToLink(Long chatId, String link) throws ApiScrapperErrorResponseException;
}
