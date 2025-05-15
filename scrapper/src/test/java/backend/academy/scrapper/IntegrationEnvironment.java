package backend.academy.scrapper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@AutoConfigureMockMvc
public abstract class IntegrationEnvironment {

    public static PostgreSQLContainer<?> postgres;

    static {
        postgres = new PostgreSQLContainer<>("postgres:17");
        postgres.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("app.github-token", () -> "test");
        registry.add("app.stackoverflow.key", () -> "test");
        registry.add("app.stackoverflow.access-token", () -> "test");
    }

    protected static void runMigrations(Connection conn) throws Exception {
        try (PostgresDatabase database = new PostgresDatabase()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE SCHEMA IF NOT EXISTS scrapper");
            }
            database.setConnection(new JdbcConnection(conn));
            database.setLiquibaseSchemaName("scrapper");
            database.setDefaultSchemaName("scrapper");
            Path migrationsPath = Paths.get("..", "migrations").toAbsolutePath().normalize();

            Scope.child("resourceAccessor", new DirectoryResourceAccessor(migrationsPath.toFile()), () -> {
                CommandScope updateCommand = new CommandScope("update")
                        .addArgumentValue("changelogFile", "changelog-master.xml")
                        .addArgumentValue("database", database);
                updateCommand.execute();
            });
        }
    }

    @BeforeAll
    protected static void beforeAll() {
        try (Connection conn =
                DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            runMigrations(conn);
            System.out.println("RunMigrationCorrect");
        } catch (Exception e) {
            throw new RuntimeException("Initial migration failed", e);
        }
    }
}
