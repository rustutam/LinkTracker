package dto.response;

import java.util.List;

/** LinkResponse */
public record LinkResponse(Long id, String url, List<String> tags, List<String> filters) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ссылка: ").append(url != null ? url : "не указана").append("\n");

        if (tags != null && !tags.isEmpty()) {
            sb.append("Теги: ").append(String.join(", ", tags)).append("\n");
        }

        if (filters != null && !filters.isEmpty()) {
            sb.append("Фильтры: ").append(String.join(", ", filters)).append("\n");
        }

        return sb.toString().trim();
    }
}
