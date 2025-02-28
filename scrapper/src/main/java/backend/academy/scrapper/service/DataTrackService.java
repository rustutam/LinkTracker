package backend.academy.scrapper.service;

import backend.academy.scrapper.client.Client;
import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.models.LinkUpdateNotification;
import backend.academy.scrapper.repository.LinksRepository;
import backend.academy.scrapper.sender.Sender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataTrackService {
    private final LinksRepository repository;
    private final Client client;
    private final Sender sender;

    @Scheduled(cron = "@hourly")
    public void updateData() {
        List<Link> allLinks = repository.getAllLinks();
        List<LinkUpdateNotification> linkUpdateNotifications = List.of();
        for (Link link: allLinks){
            String uri = link.url();
            OffsetDateTime lastUpdateDate = client.getLastUpdateDate(uri);
            if (lastUpdateDate.isAfter(link.lastUpdate())){
                //TODO обновить дату
                List<Long> allChatId = repository.getAllChatIdByLink(uri);
                linkUpdateNotifications.add(new LinkUpdateNotification(uri, allChatId));
            }
        }
        sender.send(linkUpdateNotifications);
    }
}
