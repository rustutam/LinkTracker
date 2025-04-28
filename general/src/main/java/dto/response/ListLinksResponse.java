package dto.response;

import jakarta.validation.Valid;
import java.util.List;

/** ListLinksResponse */
public record ListLinksResponse(@Valid List<@Valid LinkResponse> links, Integer size) {
//TODO убрать комменты

    //    @Override
//    public String toString() {
//        if (size == 0) {
//            return "Список отслеживаемых ссылок пуст!";
//        }
//        StringBuilder sb = new StringBuilder();
//        sb.append("Кол-во отслеживаемых ссылок: ").append(size).append("\n\n");
//        sb.append("Отслеживаемые ссылки:\n\n");
//
//        for (int i = 0; i < links.size(); i++) {
//            LinkResponse link = links.get(i);
//            sb.append(i + 1).append(") ").append(link).append("\n\n");
//        }
//
//        return sb.toString().trim();
//    }
}
