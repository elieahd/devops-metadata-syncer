package devops.platform.infrastructure.outbound.github.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GitHubWorkflowList(
        @JsonProperty("total_count") int totalCount,
        List<GitHubWorkflow> workflows
) {
}
