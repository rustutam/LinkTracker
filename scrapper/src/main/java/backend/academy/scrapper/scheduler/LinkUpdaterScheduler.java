package backend.academy.scrapper.scheduler;

import backend.academy.scrapper.service.UpdateCheckerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    UpdateCheckerServiceImpl updateCheckerService;

    @Scheduled(cron = "@hourly")
    public void update() {
        updateCheckerService.updateData();
    }


}
