package backend.academy.scrapper.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration("github")
public class GitHubConfig {
    private final String baseUrl;
    private final String githubToken;

    public GitHubConfig(ScrapperConfig scrapperConfig) {
        this.baseUrl = scrapperConfig.githubUri();
        this.githubToken = scrapperConfig.githubToken();
    }
}
