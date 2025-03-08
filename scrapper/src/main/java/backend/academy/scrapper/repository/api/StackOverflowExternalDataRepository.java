package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.client.StackoverflowClient;
import backend.academy.scrapper.exceptions.InvalidLinkException;
import backend.academy.scrapper.models.LinkMetadata;
import backend.academy.scrapper.models.external.stackoverflow.StackoverflowQuestionDTO;
import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StackOverflowExternalDataRepository implements ExternalDataRepository {
    private final StackoverflowClient stackoverflowClient;

    @Override
    public List<LinkMetadata> getLinksWithNewLastUpdateDates(List<LinkMetadata> linkList) {
        return linkList.stream()
            .filter(linkMetadata -> isProcessingUri(linkMetadata.linkUri()))
            .map(linkMetadata ->
                new LinkMetadata(
                    linkMetadata.id(),
                    linkMetadata.linkUri(),
                    getLastUpdateDate(linkMetadata.linkUri())
                )
            )
            .toList();
    }

    @Override
    public OffsetDateTime getLastUpdateDate(URI uri) {
        try {
            String questionId = getQuestionId(uri);
            StackoverflowQuestionDTO stackoverflowQuestionDTO = stackoverflowClient.questionRequest(questionId);
            Integer lastActivityDateInSeconds = stackoverflowQuestionDTO.items().stream()
                .filter(it -> questionId.equals(it.questionId()))
                .findFirst()
                .map(StackoverflowQuestionDTO.ItemResponse::lastActivityDate)
                .orElseThrow();
            return getOffsetDateTime(lastActivityDateInSeconds);
        } catch (Exception e) {
            throw new InvalidLinkException();
        }
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
