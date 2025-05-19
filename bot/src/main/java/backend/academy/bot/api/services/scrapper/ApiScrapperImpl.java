package backend.academy.bot.api.services.scrapper;

import backend.academy.bot.api.dto.AddLinkRequest;
import backend.academy.bot.api.dto.ApiErrorResponse;
import backend.academy.bot.api.dto.LinkResponse;
import backend.academy.bot.api.dto.ListLinksResponse;
import backend.academy.bot.api.dto.RemoveLinkRequest;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ApiScrapperImpl implements ApiScrapper {
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private static final String TG_CHAT_PATH = "/tg-chat";
    private static final String LINKS_PATH = "/links";
    private final RestTemplate scrapperRestTemplate;

    @Autowired
    public ApiScrapperImpl(RestTemplateBuilder scrapperRestTemplateBuilder) {
        this.scrapperRestTemplate = scrapperRestTemplateBuilder.build();
    }

    @Override
    @SuppressFBWarnings(
            value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "Я уверен, что когда я ловлю ошибку, она не является null")
    public void registerChat(Long chatId) throws ApiErrorResponse {
        try {
            UriComponents uriComponents = UriComponentsBuilder.newInstance()
                    .path(TG_CHAT_PATH)
                    .path("/{chatId}")
                    .buildAndExpand(chatId);
            HttpEntity<Object> requestEntity = new HttpEntity<>(null, null);
            scrapperRestTemplate.postForEntity(uriComponents.toUriString(), requestEntity, Void.class);
        } catch (HttpClientErrorException e) {
            throw e.getResponseBodyAs(ApiErrorResponse.class);
        }
    }

    @Override
    @SuppressFBWarnings(
            value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "Я уверен, что когда я ловлю ошибку, она не является null")
    public void deleteChat(Long chatId) throws ApiErrorResponse {
        try {
            UriComponents uriComponents = UriComponentsBuilder.newInstance()
                    .path(TG_CHAT_PATH)
                    .path("/{chatId}")
                    .buildAndExpand(chatId);
            HttpEntity<Object> requestEntity = new HttpEntity<>(null, null);
            scrapperRestTemplate.exchange(uriComponents.toUriString(), HttpMethod.DELETE, requestEntity, Void.class);
        } catch (HttpClientErrorException e) {
            throw e.getResponseBodyAs(ApiErrorResponse.class);
        }
    }

    @Override
    @SuppressFBWarnings(
            value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "Я уверен, что когда я ловлю ошибку, она не является null")
    public ListLinksResponse getLinks(Long chatId) throws ApiErrorResponse {
        try {
            UriComponents uriComponents =
                    UriComponentsBuilder.newInstance().path(LINKS_PATH).build();
            HttpHeaders headers = new HttpHeaders();
            headers.set(TG_CHAT_ID_HEADER, String.valueOf(chatId));
            HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);
            ResponseEntity<ListLinksResponse> response = scrapperRestTemplate.exchange(
                    uriComponents.toUriString(), HttpMethod.GET, requestEntity, ListLinksResponse.class);
            if (response.getStatusCode().value() == 200) {
                return response.getBody();
            }
        } catch (HttpClientErrorException e) {
            throw e.getResponseBodyAs(ApiErrorResponse.class);
        }
        return null;
    }

    @Override
    @SuppressFBWarnings(
            value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "Я уверен, что когда я ловлю ошибку, она не является null")
    public LinkResponse subscribeToLink(Long chatId, String link, List<String> tags, List<String> filters)
            throws ApiErrorResponse {
        AddLinkRequest requestToScrapper = new AddLinkRequest(link, tags, filters);
        try {
            UriComponents uriComponents =
                    UriComponentsBuilder.newInstance().path(LINKS_PATH).build();
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.set(TG_CHAT_ID_HEADER, String.valueOf(chatId));
            HttpEntity<Object> requestEntity = new HttpEntity<>(requestToScrapper, headers);
            ResponseEntity<LinkResponse> response =
                    scrapperRestTemplate.postForEntity(uriComponents.toUriString(), requestEntity, LinkResponse.class);
            if (response.getStatusCode().value() == 200) {
                return response.getBody();
            }
        } catch (HttpClientErrorException e) {
            throw e.getResponseBodyAs(ApiErrorResponse.class);
        }
        return null;
    }

    @Override
    @SuppressFBWarnings(
            value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "Я уверен, что когда я ловлю ошибку, она не является null")
    public LinkResponse unSubscribeToLink(Long chatId, String link) throws ApiErrorResponse {
        RemoveLinkRequest requestToScrapper = new RemoveLinkRequest(link);
        try {
            UriComponents uriComponents =
                    UriComponentsBuilder.newInstance().path(LINKS_PATH).build();
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.set(TG_CHAT_ID_HEADER, String.valueOf(chatId));
            HttpEntity<Object> requestEntity = new HttpEntity<>(requestToScrapper, headers);
            ResponseEntity<LinkResponse> response = scrapperRestTemplate.exchange(
                    uriComponents.toUriString(), HttpMethod.DELETE, requestEntity, LinkResponse.class);
            if (response.getStatusCode().value() == 200) {
                return response.getBody();
            }
        } catch (HttpClientErrorException e) {
            throw e.getResponseBodyAs(ApiErrorResponse.class);
        }
        return null;
    }
}
