package backend.academy.scrapper.handler;

public interface ChatHandler extends EndpointControleHandler {
    void register(long chatId);

    void unregister(long chatId);
}
