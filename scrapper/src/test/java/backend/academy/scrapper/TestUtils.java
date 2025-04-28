package backend.academy.scrapper;

import lombok.Getter;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import java.time.OffsetDateTime;
import java.util.Comparator;

public final class TestUtils {
    @Getter
    private static final RecursiveComparisonConfiguration CONFIG;

    static {
        CONFIG = RecursiveComparisonConfiguration.builder()
            .withComparatorForType(
                Comparator.comparing(OffsetDateTime::toInstant),
                OffsetDateTime.class
            )
            .build();
        CONFIG.ignoreCollectionOrder(true);
    }

    private TestUtils() {
    }
}
