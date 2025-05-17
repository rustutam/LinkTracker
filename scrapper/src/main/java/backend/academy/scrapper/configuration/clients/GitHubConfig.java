package backend.academy.scrapper.configuration.clients;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;


@Validated
@ConfigurationProperties(prefix = "app.github")
public record GitHubConfig(
    @NotEmpty String baseUri,
    @NotEmpty String token
) {

    public RestClient gitHubRestClient() {
        return RestClient.builder()
            .baseUrl(baseUri)
            .build();
    }
}

