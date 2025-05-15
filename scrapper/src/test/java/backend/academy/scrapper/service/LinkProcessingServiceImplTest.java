package backend.academy.scrapper.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.models.domain.ChangeInfo;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkChangeStatus;
import backend.academy.scrapper.repository.database.LinkRepository;
import backend.academy.scrapper.scheduler.LinkUpdaterScheduler;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
class LinkProcessingServiceImplTest extends IntegrationEnvironment {

    @Autowired
    private LinkProcessingService service;

    @Autowired
    private LinkRepository linkRepository;

    @MockitoBean
    private UpdateCheckService updateCheckService;

    @MockitoBean
    private SenderNotificationService senderNotificationService;

    @MockitoBean
    private LinkUpdaterScheduler linkUpdaterScheduler;

    @Test
    void processLinks_noPages_shouldStopWithoutProcessing() {
        service.processLinks();

        // Проверяем, что поиск был сделан один раз, а дальше exit
        verifyNoInteractions(updateCheckService, senderNotificationService);
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ProcessLinksTest() {
        // Arrange
        List<Link> links = linkRepository.findAll();

        LinkChangeStatus st0 = getLinkChangeStatusWithChange(links.getFirst());
        LinkChangeStatus st1 = getLinkChangeStatusWithoutChange(links.get(1));
        LinkChangeStatus st2 = getLinkChangeStatusWithChange(links.get(2));
        LinkChangeStatus st3 = getLinkChangeStatusWithChange(links.get(3));
        LinkChangeStatus st4 = getLinkChangeStatusWithChange(links.get(4));
        LinkChangeStatus st5 = getLinkChangeStatusWithoutChange(links.get(5));

        when(updateCheckService.detectChanges(links.getFirst())).thenReturn(st0);
        when(updateCheckService.detectChanges(links.get(1))).thenReturn(st1);
        when(updateCheckService.detectChanges(links.get(2))).thenReturn(st2);
        when(updateCheckService.detectChanges(links.get(3))).thenReturn(st3);
        when(updateCheckService.detectChanges(links.get(4))).thenReturn(st4);
        when(updateCheckService.detectChanges(links.get(5))).thenReturn(st5);
        //        doNothing().when(senderNotificationService).notifySender(any(LinkChangeStatus.class));

        // Act
        service.processLinks();

        // Assert
        verify(senderNotificationService).notifySender(st0);
        verify(senderNotificationService, never()).notifySender(st1);
        verify(senderNotificationService).notifySender(st2);
        verify(senderNotificationService).notifySender(st3);
        verify(senderNotificationService).notifySender(st4);
        verify(senderNotificationService, never()).notifySender(st5);
    }

    private LinkChangeStatus getLinkChangeStatusWithChange(Link link) {
        List<ChangeInfo> changeInfoList = List.of(new ChangeInfo(
                "Новый PR" + link.linkId().id(),
                "Добавление нового поля в бд. Время" + link.lastUpdateTime().toString(),
                "rust",
                link.createdAt(),
                "Добавил поле в доменную модель в " + link.uri().toString()));
        return new LinkChangeStatus(link, true, changeInfoList);
    }

    private LinkChangeStatus getLinkChangeStatusWithoutChange(Link link) {
        return new LinkChangeStatus(link, false, List.of());
    }
}
