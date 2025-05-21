package backend.academy.bot.api.services.scrapper;

import backend.academy.bot.api.dto.ApiErrorResponse;
import backend.academy.bot.api.dto.LinkResponse;
import backend.academy.bot.api.dto.ListLinksResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiScrapperImpl implements ApiScrapper {
    private final ScrapperRetryProxy proxy;

    @Override
    public void registerChat(Long chatId) {
        proxy.registerChat(chatId);
    }

    @Override
    @SuppressFBWarnings(
            value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "Я уверен, что когда я ловлю ошибку, она не является null")
    @Retry(name = "deleteChat")
    public void deleteChat(Long chatId) throws ApiErrorResponse {
        proxy.deleteChat(chatId);
    }

    @Override
    @SuppressFBWarnings(
            value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "Я уверен, что когда я ловлю ошибку, она не является null")
    @Retry(name = "getLinks")
    public ListLinksResponse getLinks(Long chatId) throws ApiErrorResponse {
        return proxy.getLinks(chatId);
    }

    @Override
    @SuppressFBWarnings(
            value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "Я уверен, что когда я ловлю ошибку, она не является null")
    @Retry(name = "subscribeToLink")
    public LinkResponse subscribeToLink(Long chatId, String link, List<String> tags, List<String> filters)
            throws ApiErrorResponse {
        return proxy.subscribeToLink(chatId, link, tags, filters);
    }

    @Override
    @SuppressFBWarnings(
            value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "Я уверен, что когда я ловлю ошибку, она не является null")
    @Retry(name = "unSubscribeToLink")
    public LinkResponse unSubscribeToLink(Long chatId, String link) throws ApiErrorResponse {
        return proxy.unSubscribeToLink(chatId, link);
    }
}
