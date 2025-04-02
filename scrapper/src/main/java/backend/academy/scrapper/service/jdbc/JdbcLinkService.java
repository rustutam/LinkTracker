package backend.academy.scrapper.service.jdbc;

import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkMetadata;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.repository.database.*;
import backend.academy.scrapper.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcLinkService implements LinkService {
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;
    private final TagRepository tagRepository;
    private final FilterRepository filterRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public LinkMetadata addLink(ChatId chatId, LinkMetadata link) {

        User user = chatRepository.findByChatId(chatId)
                .orElseThrow(NotExistTgChatException::new);




        Link savedLink = linkRepository.save(link.link().uri());

        subscriptionRepository.save();
        tagRepository.save();
        filterRepository.save();

        LinkMetadataRepository.


    }

    @Override
    public LinkMetadata removeLink(ChatId chatId, URI link) {

    }

    @Override
    public List<LinkMetadata> getLinks(ChatId chatId) {
        linkRepository.findAllLinksByChatId(chatId);


        return List.of();
    }
}
