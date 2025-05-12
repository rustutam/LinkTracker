package backend.academy.bot.api.cache;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisCacheStorage implements CacheStorage {
    private final RedisTemplate<String, String> template;

    @Override
    public void store(@NotNull String key, String value) {
        template.opsForValue().set(key, value);
    }

    @Override
    public boolean hasKey(@NotNull String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    @Override
    public void delete(@NotNull String key) {
        template.delete(key);
    }

    @Override
    public String get(@NotNull String key) {
        return template.opsForValue().get(key);
    }
}
