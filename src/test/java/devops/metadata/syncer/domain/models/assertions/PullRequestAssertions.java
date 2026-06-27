package devops.metadata.syncer.domain.models.assertions;

import devops.metadata.syncer.domain.models.PullRequest;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class PullRequestAssertions extends AbstractAssert<PullRequestAssertions, PullRequest> {

    public PullRequestAssertions(PullRequest actual) {
        super(actual, PullRequestAssertions.class);
    }

    public static PullRequestAssertions assertThat(PullRequest actual) {
        return new PullRequestAssertions(actual);
    }


    public PullRequestAssertions isEqualTo(PullRequest expected) {
        isNotNull();
        Assertions.assertThat(actual.number()).isEqualTo(expected.number());
        Assertions.assertThat(actual.title()).isEqualTo(expected.title());
        Assertions.assertThat(actual.state()).isEqualTo(expected.state());
        Assertions.assertThat(actual.publishedAt()).isEqualTo(expected.publishedAt());
        Assertions.assertThat(actual.mergedAt()).isEqualTo(expected.mergedAt());
        Assertions.assertThat(actual.closedAt()).isEqualTo(expected.closedAt());
        Assertions.assertThat(actual.author()).isEqualTo(expected.author());
        Assertions.assertThat(actual.isAuthorUser()).isEqualTo(expected.isAuthorUser());
        if ((actual.reviews() == null || actual.reviews().isEmpty())
                && (expected.reviews() == null || expected.reviews().isEmpty())) {
            return this;
        }
        Assertions.assertThat(actual.reviews()).hasSameSizeAs(expected.reviews());
        for (int i = 0; i < expected.reviews().size(); i++) {
            PullRequestReviewAssertions.assertThat(actual.reviews().get(i)).isEqualTo(expected.reviews().get(i));
        }
        return this;
    }
}
