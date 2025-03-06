package backend.academy.scrapper.service;

import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.repository.api.GitHubExternalDataRepository;
import backend.academy.scrapper.repository.api.StackOverflowExternalDataRepository;
import backend.academy.scrapper.repository.database.LinksRepository;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangesDetectServiceImpl implements ChangesDetectService {
    private final LinksRepository repository;
    private final GitHubExternalDataRepository gitHubExternalDataRepository;
    private final StackOverflowExternalDataRepository stackOverflowExternalDataRepository;

    public List<Link> detectChanges() {
        List<Link> linksForUpdate = new ArrayList<>();

        //Обработка ссылок с гита
        List<Link> gitHubLinks = repository.getGitHubLinks();
        Map<Long, OffsetDateTime> gitHubLastUpdateDates = gitHubExternalDataRepository.getLastUpdateDates(gitHubLinks);
        linksForUpdate.addAll(getLinksForUpdate(gitHubLinks, gitHubLastUpdateDates));

        //Обработка ссылок с ст
        List<Link> stackOverflowLinks = repository.getStackOverflowLinks();
        Map<Long, OffsetDateTime> StackLastUpdateDates = stackOverflowExternalDataRepository.getLastUpdateDates(stackOverflowLinks);
        linksForUpdate.addAll(getLinksForUpdate(stackOverflowLinks, StackLastUpdateDates));

        return linksForUpdate;
    }


    private List<Link> getLinksForUpdate(List<Link> links, Map<Long, OffsetDateTime> lastUpdateDates) {
        List<Link> updatedLinks = new ArrayList<>();

        for (Link link : links) {
            OffsetDateTime updatedTime = lastUpdateDates.get(link.id());
            if (updatedTime.isAfter(link.lastUpdateTime())) {
                updatedLinks.add(link);
                //TODO в будущем вынести обновление времени в другое место
                repository.updateLastUpdateTime(link.id(), updatedTime);
            }
        }

        return updatedLinks;
    }
}
