package backend.academy.scrapper.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration("tgBot")
public class TgBotConfig {
    private String baseUrl;

    public TgBotConfig(ScrapperConfig scrapperConfig) {
        baseUrl = scrapperConfig.baseUri();
    }
}
