package backend.academy.scrapper.configuration.clients;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;

@Validated
@ConfigurationProperties(prefix = "app.stackoverflow")
public record StackOverflowConfig(
        @NotEmpty String baseUri,
        @NotEmpty String key,
        @NotEmpty String accessToken,
        @NotNull @Min(1) Integer pageSize,
        HttpComponentsClientHttpRequestFactory httpRequestFactory
) {
    public RestClient stRestClient() {
        return RestClient.builder()
                .baseUrl(baseUri)
                .requestFactory(httpRequestFactory)
                .build();
    }
}
