package backend.academy.bot.sender;

import backend.academy.bot.api.dto.LinkResponse;
import backend.academy.bot.api.dto.ListLinksResponse;
import backend.academy.bot.client.ScrapperClient;
import backend.academy.bot.exceptions.ApiScrapperErrorResponseException;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapperHttpSender implements ScrapperSender {
    private final ScrapperClient scrapperClient;

    @Override
    public void registerChat(Long chatId) throws ApiScrapperErrorResponseException{
        scrapperClient.registerChat(chatId);
    }

    @Override
    public void deleteChat(Long chatId) throws ApiScrapperErrorResponseException {
        scrapperClient.deleteChat(chatId);
    }

    @Override
    public ListLinksResponse getLinks(Long chatId) throws ApiScrapperErrorResponseException {
        return scrapperClient.getLinks(chatId);
    }

    @Override
    public LinkResponse subscribeToLink(Long chatId, String link, List<String> tags, List<String> filters)
        throws ApiScrapperErrorResponseException {
        return scrapperClient.subscribeToLink(chatId, link, tags, filters);
    }

    @Override
    public LinkResponse unSubscribeToLink(Long chatId, String link) throws ApiScrapperErrorResponseException {
        return scrapperClient.unSubscribeToLink(chatId, link);
    }
}
