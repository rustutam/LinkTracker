package backend.academy.scrapper.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.repository.database.FilterRepository;
import backend.academy.scrapper.repository.database.LinkRepository;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import backend.academy.scrapper.repository.database.TagRepository;
import backend.academy.scrapper.repository.database.UserRepository;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "app.access-type=ORM")
class LinksControllerTest extends IntegrationEnvironment {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void whenGetLinksWithInvalidStringHeaderThenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/links").header("Tg-Chat-Id", "invalid_string")).andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void whenGetLinksThenReturnLinks() throws Exception {

        mockMvc.perform(get("/links").header("Tg-Chat-Id", "104"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.links[0].id").value(1))
                .andExpect(jsonPath("$.links[0].url").value("https://github.com/java-rustutam/semester1"))
                .andExpect(jsonPath("$.links[0].tags").isEmpty())
                .andExpect(jsonPath("$.links[0].filters").isEmpty());
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void whenGetLinkForUserWithoutLinksThenReturnEmptyList() throws Exception {

        mockMvc.perform(get("/links").header("Tg-Chat-Id", "102"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(0))
                .andExpect(jsonPath("$.links").isEmpty());
    }

    @Test
    @DirtiesContext
    void whenGetLinksWithUnauthorizedUserThenReturnUnauthorized() throws Exception {

        mockMvc.perform(get("/links").header("Tg-Chat-Id", "1000")).andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void whenAddLinksThenReturnLinkResponse() throws Exception {
        mockMvc.perform(
                        post("/links")
                                .header("Tg-Chat-Id", "102")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                    {
                        "link": "https://github.com/rustutam/TestRepo",
                        "tags": ["tag1", "tag22"],
                        "filters": ["filter1", "filter22"]
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://github.com/rustutam/TestRepo"))
                .andExpect(jsonPath("$.tags[0]").value("tag1"))
                .andExpect(jsonPath("$.tags[1]").value("tag22"))
                .andExpect(jsonPath("$.filters[0]").value("filter1"))
                .andExpect(jsonPath("$.filters[1]").value("filter22"));

        URI uri = URI.create("https://github.com/rustutam/TestRepo");
        assertTrue(linkRepository.findByUri(uri).isPresent());
        assertTrue(tagRepository.findByTag("tag1").isPresent());
        assertTrue(tagRepository.findByTag("tag22").isPresent());
        assertTrue(filterRepository.findByFilter("filter1").isPresent());
        assertTrue(filterRepository.findByFilter("filter22").isPresent());
    }

    @Test
    @DirtiesContext
    void whenAddLinksWithInvalidLinkThenReturnBadRequest() throws Exception {
        mockMvc.perform(
                        post("/links")
                                .header("Tg-Chat-Id", "102")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                    {
                        "link": "https://invalidLink.ru",
                        "tags": ["tag1", "tag22"],
                        "filters": ["filter1", "filter22"]
                    }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void whenAddLinksForUnauthorizedUserThenReturnUnauthorized() throws Exception {
        mockMvc.perform(
                        post("/links")
                                .header("Tg-Chat-Id", "1234")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                    {
                        "link": "https://github.com/rustutam/TestRepo",
                        "tags": ["tag1", "tag22"],
                        "filters": ["filter1", "filter22"]
                    }
                    """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void whenAddLinksAndLinkAlreadyTrackThenReturnPreconditionFailed() throws Exception {
        mockMvc.perform(
                        post("/links")
                                .header("Tg-Chat-Id", "100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                    {
                        "link": "https://github.com/java-rustutam/semester2",
                        "tags": ["tag1", "tag22"],
                        "filters": ["filter1", "filter22"]
                    }
                    """))
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    @DirtiesContext
    void whenDeleteLinksForUnauthorizedUserThenReturnUnauthorized() throws Exception {
        mockMvc.perform(
                        delete("/links")
                                .header("Tg-Chat-Id", "1234")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                    {
                        "link": "https://github.com/rustutam/TestRepo"
                    }
                    """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void whenDeleteLinksAndLinkNotExistThenReturnForbidden() throws Exception {
        mockMvc.perform(
                        delete("/links")
                                .header("Tg-Chat-Id", "100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                    {
                        "link": "https://github.com/rustutam/TestRepo"
                    }
                    """))
                .andExpect(status().isForbidden());
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void whenDeleteLinksAndUserNotTrackLinkThenReturnForbidden() throws Exception {
        mockMvc.perform(
                        delete("/links")
                                .header("Tg-Chat-Id", "100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                    {
                        "link": "https://github.com/java-rustutam/semester5"
                    }
                    """))
                .andExpect(status().isForbidden());
    }

    @Test
    @DirtiesContext
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void whenDeleteLinksThenReturnLinkResponse() throws Exception {
        String uri = "https://github.com/java-rustutam/semester1";

        mockMvc.perform(
                        delete("/links")
                                .header("Tg-Chat-Id", "100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                    {
                        "link": "https://github.com/java-rustutam/semester1"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.url").value(uri))
                .andExpect(jsonPath("$.tags[0]").value("tag1"))
                .andExpect(jsonPath("$.tags[1]").value("tag3"))
                .andExpect(jsonPath("$.filters[0]").value("filter1"))
                .andExpect(jsonPath("$.filters[1]").value("filter3"));

        User foundUser = userRepository.findByChatId(new ChatId(100L)).orElseThrow();

        boolean isUriContainsInUserLinks = subscriptionRepository.findByUser(foundUser).stream()
                .map(sub -> sub.link().uri().toString())
                .noneMatch(uri::equals);

        assertTrue(isUriContainsInUserLinks);
    }
}
