package dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/** AddLinkRequest */
public record AddLinkRequest(@NotNull String link, @NotNull List<String> tags, @NotNull List<String> filters) {}
