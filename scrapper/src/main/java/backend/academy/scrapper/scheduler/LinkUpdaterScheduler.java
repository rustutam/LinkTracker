package backend.academy.scrapper.scheduler;

import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.service.ChangesDetectService;
import backend.academy.scrapper.service.SenderNotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    ChangesDetectService updateCheckerService;
    SenderNotificationService senderNotificationService;

    @Scheduled(cron = "@hourly")
    public void update() {
        List<Link> updatedLinks = updateCheckerService.detectChanges();
        senderNotificationService.notifySender(updatedLinks);
    }


}
