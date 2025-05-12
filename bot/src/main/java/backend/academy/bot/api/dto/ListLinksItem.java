package backend.academy.bot.api.dto;

import java.util.List;

public record ListLinksItem(Long id, String url, List<String> tags, List<String> filters) {}
