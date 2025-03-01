package backend.academy.scrapper.service;

import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.models.LinkInfo;

import java.util.List;

public interface LinkService extends EndpointControleService {

    LinkInfo addLink(long chatId, Link link);

    LinkInfo removeLink(long chatId, String link);

    List<LinkInfo> getLinks(long chatId);
}
