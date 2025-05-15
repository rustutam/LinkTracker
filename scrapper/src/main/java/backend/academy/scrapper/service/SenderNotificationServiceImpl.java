package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.UpdatedLink;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.repository.database.LinkUpdateRepository;
import backend.academy.scrapper.sender.LinkUpdateSender;
import dto.LinkUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SenderNotificationServiceImpl implements SenderNotificationService {
    private final LinkUpdateSender linkUpdateSender;
    private final LinkUpdateRepository linkUpdateRepository;

    @Override
    @Transactional
    public void notifySender(UpdatedLink updatedLink) {
        try {
            linkUpdateSender.sendUpdates(map(updatedLink));
            linkUpdateRepository.deleteById(updatedLink.id());
        } catch (Exception ex) {
            log.atError()
                    .addKeyValue("linkUpdateId", updatedLink.id().id())
                    .addKeyValue("link", updatedLink.uri())
                    .setMessage("Ошибка отправки уведомления в sender: " + ex.getMessage())
                    .log();
        }
    }

    private LinkUpdate map(UpdatedLink updatedLink) {
        return new LinkUpdate(
                updatedLink.id().id(),
                updatedLink.uri().toString(),
                updatedLink.description(),
                updatedLink.chatIds().stream().map(ChatId::id).toList());
    }
}
