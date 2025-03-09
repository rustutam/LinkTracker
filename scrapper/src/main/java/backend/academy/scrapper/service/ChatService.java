package backend.academy.scrapper.service;

public interface ChatService extends EndpointControleService {
    void register(long chatId);

    void unRegister(long chatId);
}
