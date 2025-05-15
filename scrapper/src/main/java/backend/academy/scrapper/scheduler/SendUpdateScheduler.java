package backend.academy.scrapper.scheduler;

import backend.academy.scrapper.models.domain.UpdatedLink;
import backend.academy.scrapper.repository.database.LinkUpdateRepository;
import backend.academy.scrapper.service.SenderNotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendUpdateScheduler {
    private final SenderNotificationService senderNotificationService;
    private final LinkUpdateRepository linkUpdateRepository;

    @Scheduled(fixedDelayString = "#{sendUpdateIntervalMs}")
    public void sendUpdateJob() {
        log.atInfo().setMessage("Запуск планировщика для отправки обновлений").log();
        List<UpdatedLink> linksWithUpdate = linkUpdateRepository.findAll();
        for (var updatedLink : linksWithUpdate) {
            senderNotificationService.notifySender(updatedLink);
        }
        log.atInfo()
                .setMessage("Завершение работы планировщика для отправки обновлений")
                .log();
    }
}
