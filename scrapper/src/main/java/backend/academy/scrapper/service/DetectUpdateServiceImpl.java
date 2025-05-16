package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.ChangeInfo;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkChangeStatus;
import backend.academy.scrapper.repository.api.ExternalDataRepository;
import backend.academy.scrapper.repository.api.ExternalDataRepositoryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetectUpdateServiceImpl implements UpdateCheckService {
    private final ExternalDataRepositoryFactory repositoryFactory;

    @Override
    public LinkChangeStatus detectChanges(Link link) {
        ExternalDataRepository externalDataRepository =
                repositoryFactory.getExternalDataRepository(link.uri().toString());
        List<ChangeInfo> newContentList = externalDataRepository.getChangeInfoByLink(link).stream()
                .filter(changeInfo -> changeInfo.creationTime().isAfter(link.lastUpdateTime()))
                .toList();

        return LinkChangeStatus.builder()
                .link(link)
                .hasChanges(!newContentList.isEmpty())
                .changeInfoList(newContentList)
                .build();
    }
}
