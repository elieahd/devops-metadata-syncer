package devops.metadata.syncer.infrastructure.outbound.github.models;

import org.junit.jupiter.api.Test;

import static com.devt.randomizer.RandomizerUtils.random;
import static devops.metadata.syncer.infrastructure.outbound.github.models.GitHubModelRandomizer.aGitHubWorkflowRun;
import static org.assertj.core.api.Assertions.assertThat;

class GitHubWorkflowRunTest {

    @Test
    void isSuccess_shouldReturnTrue_whenConclusionIsSuccess() {
        // Arrange
        GitHubWorkflowRun sut = aGitHubWorkflowRun("success");
        // Act
        boolean isSuccess = sut.isSuccess();
        // Assert
        assertThat(isSuccess).isTrue();
    }

    @Test
    void isSuccess_shouldReturnFalse_whenConclusionIsNotSuccess() {
        // Arrange
        GitHubWorkflowRun sut = aGitHubWorkflowRun(random(String.class));
        // Act
        boolean isSuccess = sut.isSuccess();
        // Assert
        assertThat(isSuccess).isFalse();
    }
}
