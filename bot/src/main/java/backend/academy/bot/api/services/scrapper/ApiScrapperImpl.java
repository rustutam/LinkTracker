package backend.academy.bot.api.services.scrapper;

import backend.academy.bot.api.dto.LinkResponse;
import backend.academy.bot.api.dto.ListLinksResponse;
import backend.academy.bot.exceptions.ApiScrapperErrorResponseException;
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
    @Retry(name = "deleteChat")
    public void deleteChat(Long chatId) throws ApiScrapperErrorResponseException {
        proxy.deleteChat(chatId);
    }

    @Override
    @Retry(name = "getLinks")
    public ListLinksResponse getLinks(Long chatId) throws ApiScrapperErrorResponseException {
        return proxy.getLinks(chatId);
    }

    @Override
    @Retry(name = "subscribeToLink")
    public LinkResponse subscribeToLink(Long chatId, String link, List<String> tags, List<String> filters)
            throws ApiScrapperErrorResponseException {
        return proxy.subscribeToLink(chatId, link, tags, filters);
    }

    @Override
    @Retry(name = "unSubscribeToLink")
    public LinkResponse unSubscribeToLink(Long chatId, String link) throws ApiScrapperErrorResponseException {
        return proxy.unSubscribeToLink(chatId, link);
    }
}
