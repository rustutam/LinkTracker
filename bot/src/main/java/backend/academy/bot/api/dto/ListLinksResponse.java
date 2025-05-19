package backend.academy.bot.api.dto;

import java.util.ArrayList;

public record ListLinksResponse(int size, ArrayList<ListLinksItem> links) {}
