package backend.academy.scrapper;

import java.time.OffsetDateTime;
import java.util.Comparator;
import lombok.Getter;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;

public final class TestUtils {
    @Getter
    private static final RecursiveComparisonConfiguration CONFIG;

    static {
        CONFIG = RecursiveComparisonConfiguration.builder()
                .withComparatorForType(Comparator.comparing(OffsetDateTime::toInstant), OffsetDateTime.class)
                .build();
        CONFIG.ignoreCollectionOrder(true);
    }

    private TestUtils() {}
}
