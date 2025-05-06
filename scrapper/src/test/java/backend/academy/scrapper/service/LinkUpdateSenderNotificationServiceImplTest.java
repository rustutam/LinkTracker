package backend.academy.scrapper.service;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.TestModelFactory;
import backend.academy.scrapper.models.domain.ChangeInfo;
import backend.academy.scrapper.models.domain.LinkChangeStatus;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import backend.academy.scrapper.sender.LinkUpdateSender;
import dto.LinkUpdate;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class LinkUpdateSenderNotificationServiceImplTest extends IntegrationEnvironment {

    @MockitoBean
    private SubscriptionRepository subscriptionRepository;

    @MockitoSpyBean
    private LinkUpdateSender linkUpdateSender;

    @Autowired
    private SenderNotificationServiceImpl senderNotificationService;

    @Test
    @DisplayName("Должен отправлять уведомления, если ссылки были обновлены")
    void notifySender_ShouldSendNotification_WhenLinksUpdated() {
        // Arrange
        List<ChangeInfo> changeInfoList = List.of(new ChangeInfo(
                "Новый PR",
                "Добавление нового поля в бд",
                "rust",
                OffsetDateTime.now(),
                "Добавил поле в доменную модель "));

        String expectedDescription = changeInfoList.getFirst().toString();

        Subscription subscription = TestModelFactory.createSubscription();

        Long expectedLinkId = subscription.link().linkId().id();
        String expectedUri = subscription.link().uri().toString();
        LinkChangeStatus linkChangeStatus = new LinkChangeStatus(subscription.link(), true, changeInfoList);
        List<Long> expectedChatIds = List.of(subscription.user().chatId().id());

        List<Subscription> subscriptions = List.of(subscription);

        when(subscriptionRepository.findByLink(any())).thenReturn(subscriptions);

        doNothing().when(linkUpdateSender).pushLinkUpdate(any(LinkUpdate.class));
        // Act
        senderNotificationService.notifySender(linkChangeStatus);

        // Assert
        ArgumentCaptor<LinkUpdate> captor = ArgumentCaptor.forClass(LinkUpdate.class);
        verify(linkUpdateSender).pushLinkUpdate(captor.capture());

        LinkUpdate sent = captor.getValue();

        assertEquals(sent.id(), expectedLinkId);
        assertEquals(sent.url(), expectedUri);
        assertEquals(sent.tgChatIds(), expectedChatIds);
        assertEquals(sent.description(), expectedDescription);
    }
}
