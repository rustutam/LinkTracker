package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.ChangeInfo;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkChangeStatus;
import backend.academy.scrapper.repository.api.ExternalDataRepository;
import backend.academy.scrapper.repository.api.ExternalDataRepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateCheckServiceImpl implements UpdateCheckService {
    //    private final LinksRepository repository;
    private final ExternalDataRepositoryFactory repositoryFactory;


    @Override
    public LinkChangeStatus detectChanges(Link link) {
        ExternalDataRepository repository = repositoryFactory.getExternalDataRepository(link);

        List<ChangeInfo> contentList = repository.getChangeInfoByLink(link);

        List<ChangeInfo> newContentList = getUpdatedContent(contentList, link);

        if (newContentList.isEmpty()) {
            return LinkChangeStatus.builder()
                .link(link)
                .hasChanges(false)
                .changeInfoList(List.of())
                .build();
        }

        return LinkChangeStatus.builder()
            .link(link)
            .hasChanges(true)
            .changeInfoList(newContentList)
            .build();
    }

    private List<ChangeInfo> getUpdatedContent(List<ChangeInfo> allContent, Link link) {
        return allContent.stream().filter(changeInfo -> changeInfo.creationTime().isAfter(link.lastUpdateTime())).toList();
    }
}
