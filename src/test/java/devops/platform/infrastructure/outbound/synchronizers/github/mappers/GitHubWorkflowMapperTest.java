package devops.platform.infrastructure.outbound.synchronizers.github.mappers;

import devops.platform.domain.models.Pipeline;
import devops.platform.domain.models.PipelineRun;
import devops.platform.infrastructure.outbound.github.models.GitHubWorkflow;
import devops.platform.infrastructure.outbound.github.models.GitHubWorkflowRun;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static devops.platform.infrastructure.outbound.github.models.GitHubModelRandomizer.aGitHubWorkflow;
import static devops.platform.infrastructure.outbound.github.models.GitHubModelRandomizer.aGitHubWorkflowRun;
import static org.assertj.core.api.Assertions.assertThat;

class GitHubWorkflowMapperTest {

    private GitHubWorkflowMapper sut;

    @BeforeEach
    void setup() {
        sut = new GitHubWorkflowMapper();
    }

    @Test
    void map_shouldReturnNull_whenInputIsNull() {
        // Arrange
        GitHubWorkflow workflow = null;
        List<GitHubWorkflowRun> workflowRuns = new ArrayList<>();
        // Act
        Pipeline pipeline = sut.map(workflow, workflowRuns);
        // Assert
        assertThat(pipeline).isNull();
    }

    @Test
    void map_shouldMap() {
        // Arrange
        GitHubWorkflow workflow = aGitHubWorkflow();
        List<GitHubWorkflowRun> workflowRuns = List.of(
                aGitHubWorkflowRun(),
                aGitHubWorkflowRun()
        );
        // Act
        Pipeline pipeline = sut.map(workflow, workflowRuns);
        // Assert
        assertThat(pipeline.name()).isEqualTo(workflow.name());
        assertThat(pipeline.sourceId()).isEqualTo(String.valueOf(workflow.id()));
        assertThat(pipeline.runs()).hasSameSizeAs(workflowRuns);

        assertThatPipelineRunIsMappedFromGithub(pipeline.runs().getFirst(), workflowRuns.getFirst());
        assertThatPipelineRunIsMappedFromGithub(pipeline.runs().get(1), workflowRuns.get(1));
    }

    private void assertThatPipelineRunIsMappedFromGithub(PipelineRun run, GitHubWorkflowRun workflowRun) {
        assertThat(run.success()).isEqualTo(workflowRun.isSuccess());
        assertThat(run.startedAt()).isEqualTo(workflowRun.startedAt());
        assertThat(run.createdAt()).isEqualTo(workflowRun.createdAt());
        assertThat(run.updatedAt()).isEqualTo(workflowRun.updatedAt());
    }

    @Test
    void map_shouldReturnEmptyListOfRuns_whenRunsIsNull() {
        // Arrange
        GitHubWorkflow workflow = aGitHubWorkflow();
        List<GitHubWorkflowRun> workflowRuns = null;
        // Act
        Pipeline pipeline = sut.map(workflow, workflowRuns);
        // Assert
        assertThat(pipeline.runs())
                .isNotNull()
                .isEmpty();
    }

    @Test
    void map_shouldReturnEmptyListOfRuns_whenRunsIsEmpty() {
        // Arrange
        GitHubWorkflow workflow = aGitHubWorkflow();
        List<GitHubWorkflowRun> workflowRuns = new ArrayList<>();
        // Act
        Pipeline pipeline = sut.map(workflow, workflowRuns);
        // Assert
        assertThat(pipeline.runs())
                .isNotNull()
                .isEmpty();
    }
}
