package backend.academy.scrapper.service;

import backend.academy.scrapper.configuration.ScrapperConfig;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkChangeStatus;
import backend.academy.scrapper.repository.database.LinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkProcessingServiceImpl implements LinkProcessingService {
    private final LinkRepository linkRepository;
    private final SenderNotificationService senderNotificationService;
    private final UpdateCheckService updateCheckService;
    private final ScrapperConfig scrapperConfig;

    /**
     * Метод обрабатывает ссылки из базы данных пакетами (страницами) и проверяет наличие обновлений.
     * Если обновление обнаружено – уведомляет пользователя через SenderNotificationService.
     */
    @Override
    public void processLinks() {
        int pageNumber = 0;
        Page<Link> page;
        do {
            Pageable pageable = PageRequest.of(pageNumber, scrapperConfig.batchSize());
            page = linkRepository.findAllPaginated(pageable);

            log.atInfo()
                .setMessage("Обработка страницы " + pageNumber + ". Количество ссылок: " + page.getNumberOfElements())
                .log();

            page.getContent().forEach(this::processLink);

            pageNumber++;
        } while (page.hasNext());

        log.atInfo()
            .setMessage("Завершение обработки ссылок")
            .log();
    }

    /**
     * Обрабатывает отдельную ссылку: проверяет наличие обновлений и уведомляет пользователя при необходимости.
     *
     * @param link объект ссылки, полученный из базы данных
     */
    private void processLink(Link link) {
        try {
            // Вызываем сервис для проверки обновлений по URL ссылки
            LinkChangeStatus linkChangeStatus = updateCheckService.detectChanges(link);

            // Если получена информация об обновлении, уведомляем пользователя
            if (linkChangeStatus.hasChanges()) {
                senderNotificationService.notifySender(linkChangeStatus);
                linkRepository.updateLastUpdateTime(linkChangeStatus.link().linkId(), linkChangeStatus.link().lastUpdateTime());
                log.atInfo()
                    .addKeyValue("Найдено обновление для ссылки", link.uri().toString())
                    .setMessage("Отправка уведомления")
                    .log();
            }
        } catch (Exception ex) {
            log.atError()
                .addKeyValue("Ошибка при обработке ссылки", link.uri().toString())
                .setMessage(ex.getMessage())
                .log();
        }
    }
}
