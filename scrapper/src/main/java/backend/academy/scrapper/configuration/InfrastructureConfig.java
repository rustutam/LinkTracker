package backend.academy.scrapper.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import general.RegexCheck;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
