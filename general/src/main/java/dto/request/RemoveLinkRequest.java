package dto.request;

import jakarta.validation.constraints.NotNull;

/** RemoveLinkRequest */
public record RemoveLinkRequest(@NotNull String link) {}
