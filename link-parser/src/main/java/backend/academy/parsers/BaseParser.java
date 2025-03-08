package backend.academy.parsers;

import backend.academy.responses.BaseParseResponse;
import java.util.Optional;

public sealed abstract class BaseParser implements Parser permits GitHubParser, StackOverflowParser {
    private Parser successor;

    public void setSuccessor(Parser successor) {
        this.successor = successor;
    }

    protected BaseParseResponse nextParse(Optional<String> url) {
        return successor != null ? successor.parseUrl(url) : null;
    }
}
