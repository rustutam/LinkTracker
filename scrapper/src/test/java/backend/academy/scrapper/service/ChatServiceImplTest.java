package backend.academy.scrapper.service;

import backend.academy.scrapper.exceptions.DoubleRegistrationException;
import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.repository.database.LinksRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    @Mock
    private LinksRepository linksRepository;

    @InjectMocks
    private ChatServiceImpl chatService;

    @Test
    @DisplayName("Выбросить DoubleRegistrationException, если чат уже зарегистрирован")
    void register_ShouldThrowDoubleRegistrationException_WhenChatAlreadyExists() {
        long chatId = 123L;
        when(linksRepository.isRegistered(chatId)).thenReturn(true);

        assertThrows(DoubleRegistrationException.class, () -> chatService.register(chatId));
        verify(linksRepository, never()).register(anyLong());
    }

    @Test
    @DisplayName("Зарегистрировать чат, если его нет в базе")
    void register_ShouldRegisterChat_WhenChatDoesNotExist() {
        long chatId = 123L;
        when(linksRepository.isRegistered(chatId)).thenReturn(false);

        chatService.register(chatId);

        verify(linksRepository).register(chatId);
    }

    @Test
    @DisplayName("Выбросить NotExistTgChatException, если чата нет в базе")
    void unRegister_ShouldThrowNotExistTgChatException_WhenChatDoesNotExist() {
        long chatId = 123L;
        when(linksRepository.isRegistered(chatId)).thenReturn(false);

        assertThrows(NotExistTgChatException.class, () -> chatService.unRegister(chatId));

        verify(linksRepository, never()).unRegister(anyLong());
    }

    @Test
    @DisplayName("Удалить чат, если он есть в базе")
    void unRegister_ShouldUnregisterChat_WhenChatExists() {
        long chatId = 123L;
        when(linksRepository.isRegistered(chatId)).thenReturn(true);

        chatService.unRegister(chatId);

        verify(linksRepository).unRegister(chatId);
    }
}

