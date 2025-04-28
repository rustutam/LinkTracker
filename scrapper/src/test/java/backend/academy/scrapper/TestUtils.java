package backend.academy.scrapper;

import lombok.Getter;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;

public final class TestUtils {
    @Getter
    private static final RecursiveComparisonConfiguration CONFIG;

    static {
        CONFIG = new RecursiveComparisonConfiguration();
        CONFIG.ignoreCollectionOrder(true);
    }

    private TestUtils() {}
}
