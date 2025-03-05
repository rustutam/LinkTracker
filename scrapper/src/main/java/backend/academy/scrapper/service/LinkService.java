package backend.academy.scrapper.service;

import backend.academy.scrapper.models.Link;

import java.util.List;

public interface LinkService extends EndpointControleService {

    Link addLink(long chatId, Link link);

    Link removeLink(long chatId, String link);

    List<Link> getLinks(long chatId);
}
