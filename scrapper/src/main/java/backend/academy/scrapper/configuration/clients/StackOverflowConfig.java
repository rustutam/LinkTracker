package backend.academy.scrapper.configuration.clients;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;

@Validated
@ConfigurationProperties(prefix = "app.stackoverflow")
public record StackOverflowConfig(
    @NotEmpty String baseUri,
    @NotEmpty String key,
    @NotEmpty String accessToken
) {
    public RestClient stRestClient() {
        return RestClient.builder()
            .baseUrl(baseUri)
            .build();
    }
}
