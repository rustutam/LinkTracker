package backend.academy.scrapper.scheduler;

import backend.academy.scrapper.service.LinkProcessingService;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckUpdateScheduler {
    private static final Integer LIMIT = 500;
    private final LinkProcessingService linkProcessingService;

    @Scheduled(fixedDelayString = "#{checkUpdateIntervalMs}")
    public void checkUpdateJob() {
        log.atInfo()
                .setMessage("Запуск планировщика для проверки ссылок на обновления")
                .log();
        OffsetDateTime updateStartTime = OffsetDateTime.now();
        linkProcessingService.processLinks(LIMIT, updateStartTime);
        log.atInfo()
                .setMessage("Завершение работы планировщика проверки ссылок")
                .log();
    }
}
