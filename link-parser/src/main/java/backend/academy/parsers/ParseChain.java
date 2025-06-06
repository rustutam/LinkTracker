package backend.academy.parsers;

public class ParseChain {
    private ParseChain() {}

    public static Parser chain() {
        return chain(new GitHubParser(), new StackOverflowParser());
    }

    public static Parser chain(BaseParser firstParser, BaseParser... parsers) {
        BaseParser previous = firstParser;
        for (BaseParser current : parsers) {
            previous.setSuccessor(current);
            previous = current;
        }
        return firstParser;
    }
}
