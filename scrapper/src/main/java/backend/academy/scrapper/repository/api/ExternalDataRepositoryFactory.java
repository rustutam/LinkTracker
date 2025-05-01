package backend.academy.scrapper.repository.api;

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


    public ExternalDataRepository getExternalDataRepository(String uri) {
        return switch (uri) {
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
    }
}
