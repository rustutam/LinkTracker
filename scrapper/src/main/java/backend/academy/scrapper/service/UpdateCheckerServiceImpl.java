package backend.academy.scrapper.service;

import backend.academy.scrapper.models.LinkInfo;
import backend.academy.scrapper.repository.api.GitHubExternalDataRepository;
import backend.academy.scrapper.repository.api.StackOverflowExternalDataRepository;
import backend.academy.scrapper.repository.database.LinksRepository;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCheckerServiceImpl implements UpdateCheckerService {
    private final LinkNotificationService linkNotificationService;
    private final LinksRepository repository;
    private final GitHubExternalDataRepository gitHubExternalDataRepository;
    private final StackOverflowExternalDataRepository stackOverflowExternalDataRepository;

    public void updateData() {
        List<LinkInfo> linksForUpdate = new ArrayList<>();


        //Обработка ссылок с гита
        List<LinkInfo> gitHubLinks = repository.getGitHubLinks();
        Map<Long, OffsetDateTime> gitHubLastUpdateDates = gitHubExternalDataRepository.getLastUpdateDates(gitHubLinks);

        linksForUpdate.addAll(getLinksForUpdate(gitHubLinks, gitHubLastUpdateDates));


        //Обработка ссылок с ст
        List<LinkInfo> stackOverflowLinks = repository.getStackOverflowLinks();
        Map<Long, OffsetDateTime> StackLastUpdateDates = stackOverflowExternalDataRepository.getLastUpdateDates(stackOverflowLinks);
        linksForUpdate.addAll(getLinksForUpdate(stackOverflowLinks, StackLastUpdateDates));

        linkNotificationService.notifyAboutUpdate(linksForUpdate);


    }


    private List<LinkInfo> getLinksForUpdate(List<LinkInfo> links, Map<Long, OffsetDateTime> lastUpdateDates) {
        List<LinkInfo> updatedLinks = new ArrayList<>();


        for (LinkInfo linkInfo : links) {
            OffsetDateTime updatedTime = lastUpdateDates.get(linkInfo.id());
            if (updatedTime.isAfter(linkInfo.lastUpdateTime())) {
                updatedLinks.add(linkInfo);
                //TODO в будущем вынести обновление времени в другое место
                repository.updateLastUpdateTime(linkInfo.id(), updatedTime);
            }
        }

        return updatedLinks;
    }
}
