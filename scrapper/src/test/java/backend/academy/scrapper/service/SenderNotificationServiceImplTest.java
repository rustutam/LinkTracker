package backend.academy.scrapper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.scrapper.TestModelFactory;
import backend.academy.scrapper.models.domain.ChangeInfo;
import backend.academy.scrapper.models.domain.LinkChangeStatus;
import backend.academy.scrapper.models.domain.LinkUpdateNotification;

import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import backend.academy.scrapper.sender.Sender;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;


@SpringBootTest
class SenderNotificationServiceImplTest {

    @MockitoBean
    private SubscriptionRepository subscriptionRepository;

    @MockitoSpyBean
    private Sender sender;

    @Autowired
    private SenderNotificationServiceImpl senderNotificationService;

    @Test
    @DisplayName("Должен отправлять уведомления, если ссылки были обновлены")
    void notifySender_ShouldSendNotification_WhenLinksUpdated() {
        // Arrange
        List<ChangeInfo> changeInfoList = List.of(
            new ChangeInfo(
                "Новый PR",
                "Добавление нового поля в бд",
                "rust",
                OffsetDateTime.now(),
                "Добавил поле в доменную модель "
            )
        );

        String expectedDescription = changeInfoList.getFirst().toString();

        Subscription subscription = TestModelFactory.createSubscription();

        LinkId expectedLinkId = subscription.link().linkId();
        URI expectedUri = subscription.link().uri();
        LinkChangeStatus linkChangeStatus = new LinkChangeStatus(
            subscription.link(),
            true,
            changeInfoList
        );

        List<Subscription> subscriptions = List.of(
            subscription
        );

        when(subscriptionRepository.findByLink(any()))
            .thenReturn(subscriptions);
        doNothing().when(sender).send(any(LinkUpdateNotification.class));

        // Act
        senderNotificationService.notifySender(linkChangeStatus);

        // Assert
        ArgumentCaptor<LinkUpdateNotification> captor =
            ArgumentCaptor.forClass(LinkUpdateNotification.class);
        verify(sender).send(captor.capture());

        LinkUpdateNotification sent = captor.getValue();

        assertEquals(sent.linkId(),expectedLinkId);
        assertEquals(sent.uri(),expectedUri);
        assertEquals(sent.chatIds(), List.of(subscription.user().chatId()));
        assertEquals(sent.description(), expectedDescription);

    }
}
