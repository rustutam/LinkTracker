package backend.academy.scrapper.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration("stackoverflow")
public class StackOverflowConfig {
    private final String baseUrl;
    private final String key;
    private final String accessToken;

    public StackOverflowConfig(ScrapperConfig scrapperConfig) {
        this.baseUrl = scrapperConfig.stackOverflowUri();
        this.key = scrapperConfig.stackOverflow().key();
        this.accessToken = scrapperConfig.stackOverflow().accessToken();
    }
}
