//package backend.academy.scrapper.service.simple;
//
//import backend.academy.scrapper.exceptions.DoubleRegistrationException;
//import backend.academy.scrapper.exceptions.NotExistTgChatException;
//import backend.academy.scrapper.models.domain.ids.ChatId;
//import backend.academy.scrapper.repository.database.ChatRepository;
//import backend.academy.scrapper.repository.database.LinksRepository;
//import backend.academy.scrapper.service.ChatService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class ChatServiceImpl implements ChatService {
//    private final LinksRepository linksRepository;
//
//    @Override
//    public void register(ChatId chatId) {
//        if (linksRepository.isRegistered(chatId.id())) {
//            throw new DoubleRegistrationException();
//        }
//
//        linksRepository.register(chatId.id());
//    }
//
//    @Override
//    public void unRegister(ChatId chatId) {
//        if (!linksRepository.isRegistered(chatId.id())) {
//            throw new NotExistTgChatException();
//        }
//        linksRepository.unRegister(chatId.id());
//    }
//}
