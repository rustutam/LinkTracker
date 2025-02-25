package backend.academy.scrapper.service;

import backend.academy.scrapper.models.Link;
import java.util.List;

public interface ScrapperService {

    void register(long chatId);

    void unRegister(long chatId);

    Link addLink(long chatId, String link, List<String> tags, List<String> filters);

    Link removeLink(long chatId, String link);

    List<Link> getListLinks(long chatId);
}
