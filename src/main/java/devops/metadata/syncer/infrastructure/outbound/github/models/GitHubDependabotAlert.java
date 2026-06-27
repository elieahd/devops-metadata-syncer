package devops.metadata.syncer.infrastructure.outbound.github.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record GitHubDependabotAlert(
        int number,
        String state,
        @JsonProperty("security_vulnerability") GitHubVulnerability securityVulnerability,
        @JsonProperty("created_at") OffsetDateTime createdAt,
        @JsonProperty("updated_at") OffsetDateTime updatedAt,
        @JsonProperty("dismissed_at") OffsetDateTime dismissedAt,
        @JsonProperty("dismissed_reason") String dismissedReason,
        @JsonProperty("dismissed_comment") String dismissedComment,
        @JsonProperty("fixed_at") OffsetDateTime fixedAt
) {
}
