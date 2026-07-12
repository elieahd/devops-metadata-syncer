package devops.platform.infrastructure.outbound.synchronizers.github.mappers;

import devops.platform.domain.models.Pipeline;
import devops.platform.domain.models.PipelineRun;
import devops.platform.infrastructure.outbound.github.models.GitHubWorkflow;
import devops.platform.infrastructure.outbound.github.models.GitHubWorkflowRun;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GitHubWorkflowMapper implements Mapper {

    public Pipeline map(GitHubWorkflow workflow, List<GitHubWorkflowRun> workflowRuns) {
        if (workflow == null) {
            return null;
        }
        return new Pipeline(
                workflow.name(),
                String.valueOf(workflow.id()),
                map(workflowRuns, this::map)
        );
    }

    private PipelineRun map(GitHubWorkflowRun run) {
        return new PipelineRun(
                run.isSuccess(),
                run.startedAt(),
                run.createdAt(),
                run.updatedAt()
        );
    }
}
