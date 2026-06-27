package devops.metadata.syncer.domain.models.assertions;

import devops.metadata.syncer.domain.models.PullRequestReview;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class PullRequestReviewAssertions extends AbstractAssert<PullRequestReviewAssertions, PullRequestReview> {

    public PullRequestReviewAssertions(PullRequestReview actual) {
        super(actual, PullRequestReviewAssertions.class);
    }

    public static PullRequestReviewAssertions assertThat(PullRequestReview actual) {
        return new PullRequestReviewAssertions(actual);
    }

    public PullRequestReviewAssertions isEqualTo(PullRequestReview expected) {
        isNotNull();
        Assertions.assertThat(actual.reviewer()).isEqualTo(expected.reviewer());
        Assertions.assertThat(actual.state()).isEqualTo(expected.state());
        Assertions.assertThat(actual.submittedAt()).isEqualTo(expected.submittedAt());
        return this;
    }
}
