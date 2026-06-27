package devops.metadata.syncer.infrastructure.outbound.database;

import devops.metadata.syncer.domain.models.Project;
import devops.metadata.syncer.domain.models.Repository;
import devops.metadata.syncer.domain.models.RepositorySource;
import devops.metadata.syncer.domain.models.assertions.RepositoryAssertions;
import devops.metadata.syncer.infrastructure.OutboundDatabaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class RepositoryInventoryDatabaseAdapterTest extends OutboundDatabaseIntegrationTest {

    @Autowired
    private ProjectInventoryDatabaseAdapter projectInventory;

    @Autowired
    private RepositoryInventoryDatabaseAdapter sut;

    @Test
    void findAllByProjectId_shouldReturnRepositories() {
        // Arrange
        Project project = projectInventory.create(Project.of(random(String.class), random(String.class)));
        Project anotherProject = projectInventory.create(Project.of(random(String.class), random(String.class)));
        Repository repository1 = createRepository(project);
        Repository repository2 = createRepository(project);
        createRepository(anotherProject);
        // Act
        List<Repository> repositories = sut.findAllByProjectId(project.id());
        // Assert
        assertThat(repositories)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        RepositoryAssertions.assertThat(repositories.getFirst()).isEqualTo(repository1);
        RepositoryAssertions.assertThat(repositories.get(1)).isEqualTo(repository2);
    }

    @Test
    void findAllByProjectId_shouldReturnEmptyList_whenNoRepositoryFoundForGivenProject() {
        // Arrange
        Project project = projectInventory.create(Project.of(random(String.class), random(String.class)));
        // Act
        List<Repository> repositories = sut.findAllByProjectId(project.id());
        // Assert
        assertThat(repositories)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void updateLastSyncTime_shouldSetLastSyncTime() {
        // Arrange
        Project project = projectInventory.create(Project.of(random(String.class), random(String.class)));
        Repository repository = createRepository(project);
        LocalDateTime lastSyncTime = LocalDateTime.now().minusDays(1).plusHours(1);
        // Act
        sut.updateLastSyncTime(repository.id(), lastSyncTime);
        // Assert
        List<Repository> repositories = sut.findAllByProjectId(project.id());
        assertThat(repositories)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        assertThat(repositories.getFirst().lastSyncTime()).isCloseTo(lastSyncTime, within(Duration.ofSeconds(2)));
    }

    private Repository createRepository(Project project) {
        return sut.create(project.id(), Repository.of(random(String.class), random(String.class), random(RepositorySource.class)));
    }
}
