package devops.metadata.syncer.domain.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static devops.metadata.syncer.domain.models.ModelRandomizer.aPullRequest;
import static devops.metadata.syncer.domain.models.ModelRandomizer.aPullRequestReview;
import static org.assertj.core.api.Assertions.assertThat;

class PullRequestTest {

    @Test
    void isMerged_shouldReturnFalse_whenMergedAtIsNull() {
        // Arrange
        OffsetDateTime mergedAt = null;
        PullRequest sut = aPullRequest(mergedAt);
        // Act
        boolean isMerged = sut.isMerged();
        // Assert
        assertThat(isMerged).isFalse();
    }

    @Test
    void isMerged_shouldReturnTrue_whenMergedAtIsNotNull() {
        // Arrange
        OffsetDateTime mergedAt = OffsetDateTime.now();
        PullRequest sut = aPullRequest(mergedAt);
        // Act
        boolean isMerged = sut.isMerged();
        // Assert
        assertThat(isMerged).isTrue();
    }

    @Test
    void isReviewed_shouldReturnFalse_whenReviewsIsNull() {
        // Arrange
        List<PullRequestReview> reviews = null;
        PullRequest sut = aPullRequest(reviews);
        // Act
        boolean isReviewed = sut.isReviewed();
        // Assert
        assertThat(isReviewed).isFalse();
    }

    @Test
    void isReviewed_shouldReturnFalse_whenReviewsIsEmpty() {
        // Arrange
        List<PullRequestReview> reviews = new ArrayList<>();
        PullRequest sut = aPullRequest(reviews);
        // Act
        boolean isReviewed = sut.isReviewed();
        // Assert
        assertThat(isReviewed).isFalse();
    }

    @Test
    void isReviewed_shouldReturnTrue_whenReviewsIsNotEmpty() {
        // Arrange
        List<PullRequestReview> reviews = List.of(
                aPullRequestReview(),
                aPullRequestReview()
        );
        PullRequest sut = aPullRequest(reviews);
        // Act
        boolean isReviewed = sut.isReviewed();
        // Assert
        assertThat(isReviewed).isTrue();
    }

    private static Stream<Arguments> provideCycleTime() {

        OffsetDateTime publishedAt = OffsetDateTime.parse("2024-01-01T10:00:00Z");
        OffsetDateTime mergedAt = OffsetDateTime.parse("2024-01-03T10:00:00Z");
        OffsetDateTime closedAt = OffsetDateTime.parse("2024-01-05T10:00:00Z");

        return Stream.of(
                // publishedAt is null -> ZERO, regardless of other dates
                Arguments.of(null, null, null, Duration.ZERO),
                Arguments.of(null, mergedAt, null, Duration.ZERO),
                Arguments.of(null, null, closedAt, Duration.ZERO),
                Arguments.of(null, mergedAt, closedAt, Duration.ZERO),

                // publishedAt set, mergedAt and closedAt both null -> endDate null -> ZERO
                Arguments.of(publishedAt, null, null, Duration.ZERO),

                // publishedAt set, mergedAt null, closedAt set -> use closedAt
                Arguments.of(publishedAt, null, closedAt, Duration.between(publishedAt, closedAt)),

                // publishedAt set, mergedAt set, closedAt null -> use mergedAt
                Arguments.of(publishedAt, mergedAt, null, Duration.between(publishedAt, mergedAt)),

                // publishedAt set, both mergedAt and closedAt set -> mergedAt takes priority
                Arguments.of(publishedAt, mergedAt, closedAt, Duration.between(publishedAt, mergedAt)),

                // mergedAt before publishedAt -> negative duration
                Arguments.of(mergedAt, publishedAt, null, Duration.between(mergedAt, publishedAt)),

                // publishedAt equals mergedAt -> zero duration (but not via the null-publishedAt branch)
                Arguments.of(publishedAt, publishedAt, null, Duration.ZERO)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCycleTime")
    void cycleTime_shouldReturnDuration(OffsetDateTime publishedAt,
                                        OffsetDateTime mergedAt,
                                        OffsetDateTime closedAt,
                                        Duration expectedCycleTime) {
        // Arrange
        PullRequest sut = aPullRequest(publishedAt, mergedAt, closedAt);
        // Act
        Duration cycleTime = sut.cycleTime();
        // Assert
        assertThat(cycleTime).isEqualTo(expectedCycleTime);
    }

    private static Stream<Arguments> provideReviewTime() {

        OffsetDateTime mergedAt = OffsetDateTime.parse("2024-01-05T10:00:00Z");

        OffsetDateTime authorReviewSubmittedAt = OffsetDateTime.parse("2024-01-02T10:00:00Z");
        OffsetDateTime firstReviewerSubmittedAt = OffsetDateTime.parse("2024-01-03T10:00:00Z");
        OffsetDateTime secondReviewerSubmittedAt = OffsetDateTime.parse("2024-01-04T10:00:00Z");

        String author = "author-reviewer";
        String reviewer1 = "reviewer-1";
        String reviewer2 = "reviewer-2";

        PullRequestReview reviewByAuthor = aPullRequestReview(author, authorReviewSubmittedAt);
        PullRequestReview reviewByReviewer1 = aPullRequestReview(reviewer1, firstReviewerSubmittedAt);
        PullRequestReview reviewByReviewer2 = aPullRequestReview(reviewer2, secondReviewerSubmittedAt);
        PullRequestReview reviewWithNullUser = aPullRequestReview(null, firstReviewerSubmittedAt);

        return Stream.of(
                // mergedAt is null -> ZERO regardless of reviews
                Arguments.of(null, author, List.of(reviewByReviewer1), Duration.ZERO),

                // reviews is null -> ZERO
                Arguments.of(mergedAt, author, null, Duration.ZERO),

                // reviews is empty -> ZERO
                Arguments.of(mergedAt, author, List.of(), Duration.ZERO),

                // only review is from the author themself -> filtered out -> ZERO
                Arguments.of(mergedAt, author, List.of(reviewByAuthor), Duration.ZERO),

                // only review has null reviewer -> filtered out -> ZERO
                Arguments.of(mergedAt, author, List.of(reviewWithNullUser), Duration.ZERO),

                // single valid non-author review -> duration from that review to mergedAt
                Arguments.of(mergedAt, author, List.of(reviewByReviewer1),
                        Duration.between(firstReviewerSubmittedAt, mergedAt)),

                // multiple valid reviews -> earliest one is used
                Arguments.of(mergedAt, author, List.of(reviewByReviewer2, reviewByReviewer1),
                        Duration.between(firstReviewerSubmittedAt, mergedAt)),

                // mix of author review and reviewer reviews -> author's is ignored, earliest reviewer used
                Arguments.of(mergedAt, author, List.of(reviewByAuthor, reviewByReviewer2, reviewByReviewer1),
                        Duration.between(firstReviewerSubmittedAt, mergedAt)),

                // mix of null-reviewer review and valid reviewer review -> null-reviewer ignored
                Arguments.of(mergedAt, author, List.of(reviewWithNullUser, reviewByReviewer2),
                        Duration.between(secondReviewerSubmittedAt, mergedAt))
        );
    }

    @ParameterizedTest
    @MethodSource("provideReviewTime")
    void reviewTime_shouldReturnDuration(OffsetDateTime mergedAt,
                                         String author,
                                         List<PullRequestReview> reviews,
                                         Duration expectedReviewTime) {
        // Arrange
        PullRequest sut = aPullRequest(mergedAt, author, reviews);
        // Act
        Duration reviewTime = sut.reviewTime();
        // Assert
        assertThat(reviewTime).isEqualTo(expectedReviewTime);
    }

}
