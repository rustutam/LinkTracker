package backend.academy.bot.api.cache;

import jakarta.validation.constraints.NotNull;

public interface CacheStorage {
    void store(@NotNull String key, String value);

    boolean hasKey(@NotNull String key);

    void delete(@NotNull String key);

    String get(@NotNull String key);
}
