package devops.metadata.syncer.infrastructure.outbound.database;

import devops.metadata.syncer.domain.models.Project;
import devops.metadata.syncer.infrastructure.OutboundDatabaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        String key = random(String.class);
        String name = random(String.class);
        sut.create(Project.of(key, name));
        // Act
        Optional<Project> project = sut.findByKey(key);
        // Assert
        assertThat(project).isPresent();
        assertThat(project.get().id()).isNotNull();
        assertThat(project.get().key()).isEqualTo(key);
        assertThat(project.get().name()).isEqualTo(name);
    }

}
