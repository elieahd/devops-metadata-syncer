package devops.metadata.syncer.infrastructure.outbound.github.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record GitHubPullRequestReview(
        GitHubUser user,
        String state,
        @JsonProperty("submitted_at")
        OffsetDateTime submittedAt
) {
}
