package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkChangeStatus;
import backend.academy.scrapper.models.domain.UpdatedLink;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkUpdateDetectionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UpdateCheckService updateCheckService;

    public List<UpdatedLink> getUpdatedLinks(List<Link> links) {
        return links.stream()
                .map(link -> {
                    try {
                        return Optional.of(updateCheckService.detectChanges(link));
                    } catch (Exception ex) {
                        log.atError()
                                .addKeyValue("link", link.uri().toString())
                                .setMessage("Ошибка при получении обновлений.\n" + ex.getMessage())
                                .log();
                        return Optional.<LinkChangeStatus>empty();
                    }
                })
                .flatMap(Optional::stream)
                .filter(LinkChangeStatus::hasChanges)
                .flatMap(l -> mapToUpdatedLinks(l).stream())
                .toList();
    }

    private List<UpdatedLink> mapToUpdatedLinks(LinkChangeStatus linkChangeStatus) {
        Link link = linkChangeStatus.link();
        URI uri = link.uri();
        LinkId linkId = link.linkId();
        List<ChatId> chatIds = getChatIdsSubscribedToLink(link);
        if (chatIds.isEmpty()) {
            log.atWarn()
                    .addKeyValue("linkId", linkId)
                    .setMessage("Нет подписчиков на ссылку")
                    .log();
            return List.of();
        }

        return linkChangeStatus.changeInfoList().stream()
                .map(changeInfo -> UpdatedLink.builder()
                        .id(linkId)
                        .uri(uri)
                        .description(changeInfo.toString())
                        .chatIds(chatIds)
                        .build())
                .toList();
    }

    private List<ChatId> getChatIdsSubscribedToLink(Link link) {
        return subscriptionRepository.findByLink(link).stream()
                .map(subscription -> subscription.user().chatId())
                .toList();
    }
}
