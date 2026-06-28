package devops.metadata.syncer.domain.models;

import devops.metadata.syncer.domain.models.randomizers.PullRequestRandomizer;
import devops.metadata.syncer.domain.models.randomizers.PullRequestReviewRandomizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PullRequestTest {

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

    private static Stream<Arguments> provideReviewTime() {

        OffsetDateTime mergedAt = OffsetDateTime.parse("2024-01-05T10:00:00Z");

        OffsetDateTime authorReviewSubmittedAt = OffsetDateTime.parse("2024-01-02T10:00:00Z");
        OffsetDateTime firstReviewerSubmittedAt = OffsetDateTime.parse("2024-01-03T10:00:00Z");
        OffsetDateTime secondReviewerSubmittedAt = OffsetDateTime.parse("2024-01-04T10:00:00Z");

        String author = "author-reviewer";
        String reviewer1 = "reviewer-1";
        String reviewer2 = "reviewer-2";


        PullRequestReview reviewByAuthor = PullRequestReviewRandomizer.builder()
                .reviewer(author)
                .publishedAt(authorReviewSubmittedAt)
                .build();
        PullRequestReview reviewByReviewer1 = PullRequestReviewRandomizer.builder()
                .reviewer(reviewer1)
                .publishedAt(firstReviewerSubmittedAt)
                .build();
        PullRequestReview reviewByReviewer2 = PullRequestReviewRandomizer.builder()
                .reviewer(reviewer2)
                .publishedAt(secondReviewerSubmittedAt)
                .build();
        PullRequestReview reviewWithNullUser = PullRequestReviewRandomizer.builder()
                .reviewer(null)
                .publishedAt(firstReviewerSubmittedAt)
                .build();

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

    @Test
    void isMerged_shouldReturnFalse_whenMergedAtIsNull() {
        // Arrange
        OffsetDateTime mergedAt = null;
        PullRequest sut = PullRequestRandomizer.builder()
                .mergedAt(mergedAt)
                .build();
        // Act
        boolean isMerged = sut.isMerged();
        // Assert
        assertThat(isMerged).isFalse();
    }

    @Test
    void isMerged_shouldReturnTrue_whenMergedAtIsNotNull() {
        // Arrange
        OffsetDateTime mergedAt = OffsetDateTime.now();
        PullRequest sut = PullRequestRandomizer.builder()
                .mergedAt(mergedAt)
                .build();
        // Act
        boolean isMerged = sut.isMerged();
        // Assert
        assertThat(isMerged).isTrue();
    }

    @Test
    void isReviewed_shouldReturnFalse_whenReviewsIsNull() {
        // Arrange
        PullRequest sut = PullRequestRandomizer.builder()
                .reviews(null)
                .build();
        // Act
        boolean isReviewed = sut.isReviewed();
        // Assert
        assertThat(isReviewed).isFalse();
    }

    @Test
    void isReviewed_shouldReturnFalse_whenReviewsIsEmpty() {
        // Arrange
        PullRequest sut = PullRequestRandomizer.builder()
                .reviews(List.of())
                .build();
        // Act
        boolean isReviewed = sut.isReviewed();
        // Assert
        assertThat(isReviewed).isFalse();
    }

    @Test
    void isReviewed_shouldReturnTrue_whenReviewsIsNotEmpty() {
        // Arrange
        PullRequest sut = PullRequestRandomizer.random();
        // Act
        boolean isReviewed = sut.isReviewed();
        // Assert
        assertThat(isReviewed).isTrue();
    }

    @ParameterizedTest
    @MethodSource("provideCycleTime")
    void cycleTime_shouldReturnDuration(OffsetDateTime publishedAt,
                                        OffsetDateTime mergedAt,
                                        OffsetDateTime closedAt,
                                        Duration expectedCycleTime) {
        // Arrange
        PullRequest sut = PullRequestRandomizer.builder()
                .publishedAt(publishedAt)
                .mergedAt(mergedAt)
                .closedAt(closedAt)
                .build();
        // Act
        Duration cycleTime = sut.cycleTime();
        // Assert
        assertThat(cycleTime).isEqualTo(expectedCycleTime);
    }

    @ParameterizedTest
    @MethodSource("provideReviewTime")
    void reviewTime_shouldReturnDuration(OffsetDateTime mergedAt,
                                         String author,
                                         List<PullRequestReview> reviews,
                                         Duration expectedReviewTime) {
        // Arrange
        PullRequest sut = PullRequestRandomizer.builder()
                .author(author)
                .mergedAt(mergedAt)
                .reviews(reviews)
                .build();
        // Act
        Duration reviewTime = sut.reviewTime();
        // Assert
        assertThat(reviewTime).isEqualTo(expectedReviewTime);
    }

}
