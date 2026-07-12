package devops.platform.infrastructure.outbound.github.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GitHubWorkflowRunList(
        @JsonProperty("total_count")
        int totalCount,

        @JsonProperty("workflow_runs")
        List<GitHubWorkflowRun> runs) {
}
