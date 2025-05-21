package backend.academy.bot.config.clients;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;

@Validated
@ConfigurationProperties(prefix = "app.scrapper")
public record ScrapperConfig(@NotEmpty String baseUri, HttpComponentsClientHttpRequestFactory httpRequestFactory) {
    public RestClient restClient() {
        return RestClient.builder()
            .baseUrl(baseUri)
            .requestFactory(httpRequestFactory)
            .build();
    }
}
