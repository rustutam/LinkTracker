package backend.academy.bot.api.dto;

import java.util.List;

public record AddLinkRequest(String link, List<String> tags, List<String> filters) {}
