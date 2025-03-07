package backend.academy.scrapper.service;

import backend.academy.scrapper.models.LinkMetadata;
import backend.academy.scrapper.repository.api.GitHubExternalDataRepository;
import backend.academy.scrapper.repository.api.StackOverflowExternalDataRepository;
import backend.academy.scrapper.repository.database.LinksRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangesDetectServiceImpl implements ChangesDetectService {
    private final LinksRepository repository;
    private final GitHubExternalDataRepository gitHubExternalDataRepository;
    private final StackOverflowExternalDataRepository stackOverflowExternalDataRepository;


    public List<LinkMetadata> detectChanges() {
        List<LinkMetadata> updatedLinks = new ArrayList<>();
        // Обработка ссылок с гита
        List<LinkMetadata> gitHubLinksWithOldDate = repository.getGitHubLinks();
        List<LinkMetadata> gitHubLinksWithNewDate = gitHubExternalDataRepository.getLinkLastUpdateDates(gitHubLinksWithOldDate);
        updatedLinks.addAll(getUpdatedLink(gitHubLinksWithOldDate, gitHubLinksWithNewDate));

        // Обработка ссылок с ст
        List<LinkMetadata> stackOverflowLinks = repository.getStackOverflowLinks();
        List<LinkMetadata> StackLastUpdateDates = stackOverflowExternalDataRepository.getLinkLastUpdateDates(stackOverflowLinks);
        updatedLinks.addAll(getUpdatedLink(stackOverflowLinks, StackLastUpdateDates));

        repository.updateLinksLastUpdateTime(updatedLinks);

        return updatedLinks;
    }


    private List<LinkMetadata> getUpdatedLink(List<LinkMetadata> linksWithOldDate, List<LinkMetadata> linksWithNewDate) {
        return linksWithNewDate.stream()
            .filter(newLink -> linksWithOldDate.stream()
                .anyMatch(oldLink ->
                    oldLink.linkUri().equals(newLink.linkUri())
                        && newLink.lastUpdateTime().isAfter(oldLink.lastUpdateTime())
                )
            )
            .toList();
    }
}
