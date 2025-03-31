package backend.academy.scrapper.service;

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

    //TODO подтягивать из конфига
    private final int pageSize = 500;

    /**
     * Метод обрабатывает ссылки из базы данных пакетами (страницами) и проверяет наличие обновлений.
     * Если обновление обнаружено – уведомляет пользователя через SenderNotificationService.
     */
    @Override
    public void processLinks() {
        int pageNumber = 0;
        while (true) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Link> page = linkRepository.findAll(pageable);

            if (!page.hasNext()) {
                log.info("Завершение обработки ссылок");
                return;

            }
            log.info("Обработка страницы {}. Количество ссылок: {}", pageNumber, page.getNumberOfElements());

            // Обрабатываем каждую ссылку на текущей странице
            page.getContent().forEach(this::processLink);
            pageNumber++;
        }

//        // Цикл, выполняющий пагинацию до тех пор, пока есть следующие страницы
//        do {
//            // Создаём объект Pageable для запроса страницы с нужным размером
//            Pageable pageable = PageRequest.of(pageNumber, pageSize);
//            page = linkRepository.findAll(pageable);
//            log.info("Обработка страницы {}. Количество ссылок: {}", pageNumber, page.getNumberOfElements());
//
//            // Обрабатываем каждую ссылку на текущей странице
//            page.getContent().forEach(link -> processLink(link));
//
//            pageNumber++;
//        } while (page.hasNext());
    }

    /**
     * Обрабатывает отдельную ссылку: проверяет наличие обновлений и уведомляет пользователя при необходимости.
     *
     * @param link объект ссылки, полученный из базы данных
     */
    private void processLink(Link link) {
        try {
            // Вызываем сервис для проверки обновлений по URL ссылки
            LinkChangeStatus linkChangeStatus =  updateCheckService.detectChanges(link);

            // Если получена информация об обновлении, уведомляем пользователя
            if (linkChangeStatus.hasChanges()) {
                log.info("Найдено обновление для ссылки {}. Отправка уведомления...", link.uri().toString());
                senderNotificationService.notifySender(linkChangeStatus);
            }
        } catch (Exception ex) {
            log.error("Ошибка при обработке ссылки {}: {}", link.uri().toString(), ex.getMessage());
        }
    }
}
