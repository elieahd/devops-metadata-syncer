package devops.metadata.syncer.infrastructure.outbound.github.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record GitHubWorkflowRun(String status,
                                String conclusion,
                                @JsonProperty("run_started_at")
                                OffsetDateTime startedAt,

                                @JsonProperty("created_at")
                                OffsetDateTime createdAt,

                                @JsonProperty("updated_at")
                                OffsetDateTime updatedAt) {

    public boolean isSuccess() {
        return "success".equals(conclusion);
    }
}
