package backend.academy.bot.client;

import backend.academy.bot.api.dto.LinkResponse;
import backend.academy.bot.api.dto.ListLinksResponse;
import backend.academy.bot.exceptions.ApiScrapperErrorResponseException;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScrapperClient {
    private final ScrapperRetryProxy proxy;

    public void registerChat(Long chatId) throws ApiScrapperErrorResponseException{
        proxy.registerChat(chatId);
    }

    @Retry(name = "deleteChat")
    public void deleteChat(Long chatId) throws ApiScrapperErrorResponseException {
        proxy.deleteChat(chatId);
    }


    @Retry(name = "getLinks")
    public ListLinksResponse getLinks(Long chatId) throws ApiScrapperErrorResponseException {
        return proxy.getLinks(chatId);
    }


    @Retry(name = "subscribeToLink")
    public LinkResponse subscribeToLink(Long chatId, String link, List<String> tags, List<String> filters)
        throws ApiScrapperErrorResponseException {
        return proxy.subscribeToLink(chatId, link, tags, filters);
    }


    @Retry(name = "unSubscribeToLink")
    public LinkResponse unSubscribeToLink(Long chatId, String link) throws ApiScrapperErrorResponseException {
        return proxy.unSubscribeToLink(chatId, link);
    }
}
