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
        String uri = link.uri().toString();

        ExternalDataRepository repository = switch (uri) {
            case String u when regexCheck.isGithub(u) -> gitHubRepository;
            case String u when regexCheck.isStackOverflow(u) -> stackOverflowRepository;
            default -> {
                log.atError()
                    .addKeyValue("link", uri)
                    .setMessage("Неподдерживаемая ссылка")
                    .log();
                throw new IllegalArgumentException("Неподдерживаемая ссылка");
            }
        };

        return repository;
    }
}
