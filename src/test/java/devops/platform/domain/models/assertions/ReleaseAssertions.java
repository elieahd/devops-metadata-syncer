package devops.platform.domain.models.assertions;

import devops.platform.domain.models.Release;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ReleaseAssertions extends AbstractAssert<ReleaseAssertions, Release> {

    public ReleaseAssertions(Release actual) {
        super(actual, ReleaseAssertions.class);
    }

    public static ReleaseAssertions assertThat(Release actual) {
        return new ReleaseAssertions(actual);
    }

    public ReleaseAssertions isEqualTo(Release expected) {

        isNotNull();

        Assertions.assertThat(actual.name()).isEqualTo(expected.name());
        Assertions.assertThat(actual.tagName()).isEqualTo(expected.tagName());
        Assertions.assertThat(actual.publishedAt()).isEqualTo(expected.publishedAt());

        return this;
    }
}
