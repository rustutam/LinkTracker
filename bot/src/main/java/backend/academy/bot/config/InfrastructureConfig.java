package backend.academy.bot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import general.ExceptionUtils;
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

    @Bean
    public ExceptionUtils exceptionUtils() {return new ExceptionUtils();}
}
