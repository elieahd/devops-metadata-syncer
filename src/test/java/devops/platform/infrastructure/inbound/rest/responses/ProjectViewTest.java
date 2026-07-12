package devops.platform.infrastructure.inbound.rest.responses;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.randomizers.ProjectRandomizer;
import devops.platform.infrastructure.inbound.rest.responses.ProjectView;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectViewTest {

    @Test
    void map_shouldReturnNull_whenProjectIsNull() {
        // Arrange
        Project project = null;
        // Act
        ProjectView projectView = ProjectView.map(project);
        // Assert
        assertThat(projectView).isNull();
    }

    @Test
    void map_shouldMapKey() {
        // Arrange
        Project project = ProjectRandomizer.random();
        // Act
        ProjectView projectView = ProjectView.map(project);
        // Assert
        assertThat(projectView).isNotNull();
        assertThat(projectView.key()).isEqualTo(project.key());
    }

    @Test
    void map_shouldMapName() {
        // Arrange
        Project project = ProjectRandomizer.random();
        // Act
        ProjectView projectView = ProjectView.map(project);
        // Assert
        assertThat(projectView).isNotNull();
        assertThat(projectView.name()).isEqualTo(project.name());
    }
}
