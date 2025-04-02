package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.models.domain.Link;
import general.RegexCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalDataRepositoryFactory {
    private final RegexCheck regexCheck;
    private final GitHubExternalDataRepository gitHubRepository;
    private final StackOverflowExternalDataRepository stackOverflowRepository;


    public ExternalDataRepository getExternalDataRepository(Link link) {
        //TODO переписать красиво
        String uri = link.uri().toString();

        if (regexCheck.isGithub(uri)){
            return gitHubRepository;
        }
        else if (regexCheck.isStackOverflow(uri)){
            return stackOverflowRepository;
        }
        else {
            log.atError()
                .addKeyValue("link", uri)
                .setMessage("Неподдерживаемая ссылка")
                .log();
            throw new IllegalArgumentException("Неподдерживаемая ссылка");
        }
    }
}
