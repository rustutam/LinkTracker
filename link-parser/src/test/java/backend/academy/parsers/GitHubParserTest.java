package backend.academy.parsers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import backend.academy.GeneralParseLink;
import backend.academy.responses.GitHubParseResponse;
import java.util.Optional;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class GitHubParserTest {
    @ParameterizedTest(name = "{index} - {0} is a bad link")
    @NullAndEmptySource
    void linkIsNullOrEmpty_Null(String link) {
        // given

        // when
        GitHubParseResponse response = (GitHubParseResponse) new GeneralParseLink().start(link);

        // then
        assertNull(response);
    }

    @ParameterizedTest(name = "{index} - {0} is a bad link")
    @ValueSource(
            strings = {
                "https://github.com//T_projest/pull/3",
                "https://githb.com/Name/T_projest/pull/3",
                "https://githb.com",
                "https://githb.com/Name",
                "https://githb.com/Name/",
                "https://githb.com/Name//"
            })
    void invalidLink_Null(String link) {
        // given

        // when
        GitHubParseResponse response = (GitHubParseResponse) new GeneralParseLink().start(link);

        // then
        assertNull(response);
    }

    @ParameterizedTest(name = "{index} - {0} is a valid link")
    @ValueSource(
            strings = {
                "https://github.com/TestRepo/T_projest/pull/3",
                "https://github.com/TestRepo/T_projest/",
                "https://github.com/TestRepo/T_projest",
            })
    void validLink_OK(String link) {
        // given

        // when
        GitHubParseResponse response = (GitHubParseResponse) new GeneralParseLink().start(link);

        // then
        assertAll(() -> assertEquals(response.repo(), "T_projest"), () -> assertEquals(response.user(), "TestRepo"));
    }

    @ParameterizedTest()
    @ValueSource(
            strings = {
                "https://github.com/Gram3r/Tinkoff-tourism",
                "https://github.com/Gram3r/Tinkoff-tourism/blob/main/src/main/java/tinkoff/tourism/controller/dto/RouteRequest.java",
            })
    void changeParserChain_OK(String link) {
        // given
        Parser parseChain = ParseChain.chain(new StackOverflowParser(), new GitHubParser());

        // when
        GitHubParseResponse response = (GitHubParseResponse) parseChain.parseUrl(Optional.ofNullable(link));

        // then
        assertAll(
                () -> assertEquals(response.user(), "Gram3r"), () -> assertEquals(response.repo(), "Tinkoff-tourism"));
    }
}
