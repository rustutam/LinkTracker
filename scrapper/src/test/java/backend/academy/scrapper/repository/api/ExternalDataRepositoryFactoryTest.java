package backend.academy.scrapper.repository.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import general.RegexCheck;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ExternalDataRepositoryFactoryTest {
    @Autowired
    private RegexCheck regexCheck;

    @Autowired
    private ExternalDataRepositoryFactory repositoryFactory;

    @Test
    void shouldReturnGitHubRepositoryForGithubLinks() {
        // Arrange
        String githubUri = "https://github.com/user/repo";

        // Act
        ExternalDataRepository result = repositoryFactory.getExternalDataRepository(githubUri);

        // Assert
        assertInstanceOf(GitHubExternalDataRepository.class, result);
    }

    @Test
    void shouldReturnStackOverflowRepositoryForStackoverflowLinks() {
        // Arrange
        String stackoverflowUri = "https://stackoverflow.com/questions/123";

        // Act
        ExternalDataRepository result = repositoryFactory.getExternalDataRepository(stackoverflowUri);

        // Assert
        assertInstanceOf(StackOverflowExternalDataRepository.class, result);
    }

    @Test
    void shouldThrowExceptionForUnsupportedLinks() {
        // Arrange
        String invalidUri = "https://google.com";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> repositoryFactory.getExternalDataRepository(invalidUri));

        assertEquals("Неподдерживаемая ссылка", exception.getMessage());
    }
}
