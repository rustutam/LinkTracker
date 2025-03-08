package backend.academy.parsers;

import backend.academy.responses.BaseParseResponse;
import java.util.Optional;

public interface Parser {
    BaseParseResponse parseUrl(Optional<String> url);
}
