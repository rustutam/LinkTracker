package backend.academy.scrapper.models.domain;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class ChangeInfo {
    private String description;
    private String title;
    private String username;
    private OffsetDateTime creationTime;
    private String preview;

    @Override
    public String toString() {

        return description + ". " + title + ".\n" +
            "Автор: " + username + ". Дата: " + creationTime +
            ".\n Описание: " + preview;
    }
}
