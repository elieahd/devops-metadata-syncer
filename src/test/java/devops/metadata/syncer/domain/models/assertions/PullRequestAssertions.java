package devops.metadata.syncer.domain.models.assertions;

import devops.metadata.syncer.domain.models.PullRequest;
import devops.metadata.syncer.domain.models.PullRequestReview;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.List;

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

        if (isNullOrEmpty(actual.reviews()) && isNullOrEmpty(expected.reviews())) {
            return this;
        }

        Assertions.assertThat(actual.reviews())
                .isNotNull()
                .isNotEmpty()
                .hasSameSizeAs(expected.reviews());

        for (int i = 0; i < expected.reviews().size(); i++) {
            PullRequestReview review = actual.reviews().get(i);
            PullRequestReview expectedReview = expected.reviews().get(i);
            PullRequestReviewAssertions.assertThat(review).isEqualTo(expectedReview);
        }

        return this;
    }


    private boolean isNullOrEmpty(List<PullRequestReview> reviews) {
        return reviews == null || reviews.isEmpty();
    }
}
