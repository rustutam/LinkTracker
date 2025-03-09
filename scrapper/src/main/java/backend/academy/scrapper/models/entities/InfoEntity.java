package backend.academy.scrapper.models.entities;

import java.util.List;

public record InfoEntity(long infoId, List<String> tags, List<String> filters) {}
