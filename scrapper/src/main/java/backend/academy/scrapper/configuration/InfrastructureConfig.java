package backend.academy.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import general.RegexCheck;

@Configuration
public class InfrastructureConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RegexCheck regexCheck() {
        return new RegexCheck();
    }
}
