package devops.metadata.syncer.infrastructure.outbound.synchronizers.github.mappers;

import devops.metadata.syncer.domain.models.Pipeline;
import devops.metadata.syncer.domain.models.PipelineRun;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubWorkflow;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubWorkflowRun;
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
