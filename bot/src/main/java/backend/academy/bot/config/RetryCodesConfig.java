package backend.academy.bot.config;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@ConfigurationProperties(prefix = "retry-codes")
public record RetryCodesConfig(@NotEmpty List<String> instances, @NotEmpty List<Integer> codes) {
    @Component
    public static class RetryConfigurator {

        public RetryConfigurator(RetryRegistry retryRegistry, RetryCodesConfig config) {
            for (var instance : config.instances()) {
                Retry existingRetry = retryRegistry.retry(instance);

                RetryConfig newConfig = RetryConfig.from(existingRetry.getRetryConfig())
                        .retryOnException(throwable -> {
                            if (throwable instanceof ResourceAccessException) {
                                return true;
                            }
                            if (throwable instanceof HttpServerErrorException) {
                                int code = Integer.parseInt(throwable.getMessage());
                                return config.codes().contains(code);
                            }
                            return false;
                        })
                        .build();
                retryRegistry.remove(existingRetry.getName());
                retryRegistry.retry(existingRetry.getName(), newConfig);
            }
        }
    }
}
