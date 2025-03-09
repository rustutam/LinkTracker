package backend.academy;

import backend.academy.parsers.ParseChain;
import backend.academy.parsers.Parser;
import backend.academy.responses.BaseParseResponse;
import java.util.Optional;

public class GeneralParseLink {
    private static final Parser PARSER = ParseChain.chain();

    public BaseParseResponse start(String link) {
        // Chain of responsibility
        return PARSER.parseUrl(Optional.ofNullable(link));
    }
}
