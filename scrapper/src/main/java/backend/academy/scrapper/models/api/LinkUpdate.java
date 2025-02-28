package backend.academy.scrapper.models.api;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * LinkUpdate
 */
public record LinkUpdate(
    Long id,
    String url,
    String description,
    @Valid
    List<Long> tgChatIds
) {
}

