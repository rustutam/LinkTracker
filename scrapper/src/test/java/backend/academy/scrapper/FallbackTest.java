package backend.academy.scrapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import backend.academy.scrapper.sender.BotHttpSender;
import backend.academy.scrapper.sender.BotKafkaSender;
import backend.academy.scrapper.sender.ResistantProxy;
import dto.LinkUpdate;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAutoConfiguration
class FallbackTest {
    @Autowired
    private ResistantProxy proxy;

    @MockitoBean
    public BotHttpSender httpMock = mock(BotHttpSender.class);

    @MockitoBean
    public BotKafkaSender kafkaMock = mock(BotKafkaSender.class);

    @Test
    void fallback() {
        LinkUpdate linkUpdate = new LinkUpdate(1L, "", "req", new ArrayList<>());

        assertTrue("msg", AopUtils.isAopProxy(proxy));
        doThrow(new RuntimeException()).when(httpMock).sendUpdates(any());

        proxy.sendUpdates(linkUpdate);

        verify(kafkaMock, timeout(10000)).sendUpdates(linkUpdate);
    }
}
