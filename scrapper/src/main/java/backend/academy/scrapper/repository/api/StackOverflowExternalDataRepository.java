package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.client.StackoverflowClient;
import backend.academy.scrapper.models.LinkMetadata;
import backend.academy.scrapper.models.external.stackoverflow.StackoverflowQuestionDTO;
import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StackOverflowExternalDataRepository implements ExternalDataRepository {
    private final StackoverflowClient stackoverflowClient;

    @Override
    public List<LinkMetadata> getLinkLastUpdateDates(List<LinkMetadata> linkList) {
        return linkList.stream()
            .filter(linkMetadata -> isProcessingUri(linkMetadata.linkUri()))
            .map(linkMetadata ->
                new LinkMetadata(
                    linkMetadata.id(),
                    linkMetadata.linkUri(),
                    getLinkUpdatedDate(linkMetadata.linkUri())
                )
            )
            .toList();
    }

    private OffsetDateTime getLinkUpdatedDate(URI uri) {
        String questionId = getQuestionId(uri);
        StackoverflowQuestionDTO stackoverflowQuestionDTO = stackoverflowClient.questionRequest(questionId);
        Integer lastActivityDateInSeconds = stackoverflowQuestionDTO.items().stream()
            .filter(it -> questionId.equals(it.questionId()))
            .findFirst()
            .map(StackoverflowQuestionDTO.ItemResponse::lastActivityDate)
            .orElseThrow();
        return getOffsetDateTime(lastActivityDateInSeconds);
    }

    private OffsetDateTime getOffsetDateTime(Integer lastActivityDateInSeconds) {
        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(lastActivityDateInSeconds), ZoneOffset.UTC);
    }

    private String getQuestionId(URI repoUrl) {
        String[] parts = repoUrl.getPath().split("/");
        return parts[2];
    }


    @Override
    public boolean isProcessingUri(URI uri) {
        return uri.getHost().equals("stackoverflow.com");
    }
}
