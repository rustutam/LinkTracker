package backend.academy.scrapper.configuration.clients;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;

@Validated
@ConfigurationProperties(prefix = "app.bot")
public record BotConfig(
    @NotEmpty String baseUri
) {
    public RestClient botRestClient() {
        return RestClient.builder()
            .baseUrl(baseUri)
            .build();
    }
}
