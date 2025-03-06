package backend.academy.scrapper.scheduler;

import backend.academy.scrapper.models.LinkMetadata;
import backend.academy.scrapper.service.ChangesDetectService;
import backend.academy.scrapper.service.SenderNotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final ChangesDetectService changesDetectService;
    private final SenderNotificationService senderNotificationService;

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        log.info("Updating the link on a schedule");
        List<LinkMetadata> updatedLinks = changesDetectService.detectChanges();
        senderNotificationService.notifySender(updatedLinks);

    }


}
