package devops.platform.infrastructure.outbound.database;

import devops.platform.domain.models.Project;
import devops.platform.infrastructure.OutboundDatabaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;

class ProjectInventoryDatabaseAdapterTest extends OutboundDatabaseIntegrationTest {

    @Autowired
    private ProjectInventoryDatabaseAdapter sut;

    @Test
    void findByKey_shouldReturnOptionalOfEmpty_whenNotFound() {
        // Arrange
        String key = random(String.class);
        // Act
        Optional<Project> project = sut.findByKey(key);
        // Assert
        assertThat(project).isEmpty();
    }

    @Test
    void findByKey_shouldReturnOptionalOfProject_whenProjectExists() {
        // Arrange
        Project existingProject = createProject();
        // Act
        Optional<Project> project = sut.findByKey(existingProject.key());
        // Assert
        assertThat(project).isPresent();
        assertThat(project.get().id()).isNotNull();
        assertThat(project.get().key()).isEqualTo(existingProject.key());
        assertThat(project.get().name()).isEqualTo(existingProject.name());
    }

    @Test
    void existsByKey_shouldReturnTrue_whenProjectExists() {
        // Arrange
        Project project = createProject();
        // Act
        boolean exists = sut.existsByKey(project.key());
        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void existsByKey_shouldReturnFalse_whenProjectDoesNotExists() {
        // Arrange
        String key = random(String.class);
        // Act
        boolean exists = sut.existsByKey(key);
        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    @Sql(scripts = {"/sql/clean-up.sql"})
    void findAll_shouldReturnAllProjects() {
        // Arrange
        Project project1 = createProject();
        Project project2 = createProject();
        Project project3 = createProject();
        // Act
        List<Project> projects = sut.findAll();
        // Assert
        assertThat(projects)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .contains(project1, project2, project3);
    }

    private Project createProject() {
        String key = random(String.class);
        String name = random(String.class);
        return sut.create(Project.of(key, name));
    }
}
