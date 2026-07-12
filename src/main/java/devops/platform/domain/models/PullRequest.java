package devops.platform.domain.models;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

public record PullRequest(int number,
                          String title,
                          String state,
                          OffsetDateTime publishedAt,
                          OffsetDateTime mergedAt,
                          OffsetDateTime closedAt,
                          String author,
                          boolean isAuthorUser,
                          List<PullRequestReview> reviews) {

    public boolean isMerged() {
        return mergedAt != null;
    }

    public boolean isReviewed() {
        return reviews != null && !reviews.isEmpty();
    }

    public Duration cycleTime() {
        if (publishedAt == null) {
            return Duration.ZERO;
        }
        OffsetDateTime endDate = mergedAt != null
                ? mergedAt
                : closedAt;
        if (endDate == null) {
            return Duration.ZERO;
        }
        return Duration.between(publishedAt, endDate);
    }

    public Duration reviewTime() {
        if (mergedAt == null || reviews == null || reviews.isEmpty()) {
            return Duration.ZERO;
        }
        return reviews.stream()
                .filter(review -> review.reviewer() != null && !review.reviewer().equals(author))
                .min(Comparator.comparing(PullRequestReview::submittedAt))
                .map(firstReview -> Duration.between(firstReview.submittedAt(), mergedAt))
                .orElse(Duration.ZERO);
    }

}
