package devops.platform.domain.models;

import java.time.OffsetDateTime;

public record PullRequestReview(String reviewer,
                                String state,
                                OffsetDateTime submittedAt) {
}
