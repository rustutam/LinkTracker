package dto;

import jakarta.validation.Valid;
import java.util.List;

/** LinkUpdate */
public record LinkUpdate(Long id, String url, String description, @Valid List<Long> tgChatIds) {}
