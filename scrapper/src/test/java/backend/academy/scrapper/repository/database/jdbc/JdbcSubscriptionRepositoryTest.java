package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.repository.database.SubscriptionRepositoryTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "app.access-type=SQL")
public class JdbcSubscriptionRepositoryTest extends SubscriptionRepositoryTest {}
