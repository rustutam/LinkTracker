package backend.academy.scrapper.scheduler;

import backend.academy.scrapper.service.LinkProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private static final Integer LIMIT = 500;
    private final LinkProcessingService linkProcessingService;

    @Scheduled(fixedDelayString = "#{schedulerIntervalMs}")
    public void update() {
        log.atInfo().setMessage("Запуск планировщика обновления ссылок").log();
        linkProcessingService.processLinks(LIMIT);
        log.atInfo().setMessage("Завершение обработки ссылок").log();
    }
}
