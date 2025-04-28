package backend.academy.scrapper.repository.database.jpa;

import backend.academy.scrapper.repository.database.TagRepositoryTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "app.access-type=ORM")
public class JpaTagRepositoryTest extends TagRepositoryTest {
}
