package devops.platform.infrastructure.outbound.database;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.Repository;
import devops.platform.domain.models.RepositorySource;
import devops.platform.domain.models.assertions.RepositoryAssertions;
import devops.platform.infrastructure.OutboundDatabaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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

    @Test
    void findOneByOrganizationAndNameAndSource_shouldReturnOptionalEmpty_whenNotFound() {
        // Arrange
        String organization = random(String.class);
        String name = random(String.class);
        RepositorySource source = random(RepositorySource.class);
        // Act
        Optional<Repository> repository = sut.findOneByOrganizationAndNameAndSource(organization, name, source);
        // Assert
        assertThat(repository).isEmpty();
    }

    @Test
    void findOneByOrganizationAndNameAndSource_shouldReturnOptionalOfRepository_evenIfItsCaseInsensitive() {
        // Arrange
        Project project = projectInventory.create(Project.of(random(String.class), random(String.class)));
        Repository existingRepository = createRepository(project);

        String organization = existingRepository.organization().toLowerCase(Locale.ROOT);
        String name = existingRepository.name().toUpperCase(Locale.ROOT);
        RepositorySource source = existingRepository.source();
        // Act
        Optional<Repository> repository = sut.findOneByOrganizationAndNameAndSource(organization, name, source);
        // Assert
        assertThat(repository).isPresent();
        RepositoryAssertions.assertThat(repository.get()).isEqualTo(existingRepository);
    }

    @Test
    void findAllByProjectKey_shouldReturnEmptyList_whenNoRepositoryFound() {
        // Arrange
        Project project = projectInventory.create(Project.of(random(String.class), random(String.class)));
        // Act
        List<Repository> repositories = sut.findAllByProjectKey(project.key());
        // Assert
        assertThat(repositories)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void findAllByProjectKey_shouldReturnRepositoryByProject() {
        // Arrange
        Project project = projectInventory.create(Project.of(random(String.class), random(String.class)));
        Project anotherProject = projectInventory.create(Project.of(random(String.class), random(String.class)));
        Repository repository1 = createRepository(project);
        Repository repository2 = createRepository(project);
        createRepository(anotherProject);
        // Act
        List<Repository> repositories = sut.findAllByProjectKey(project.key());
        // Assert
        assertThat(repositories)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        RepositoryAssertions.assertThat(repositories.getFirst()).isEqualTo(repository1);
        RepositoryAssertions.assertThat(repositories.get(1)).isEqualTo(repository2);
    }

    private Repository createRepository(Project project) {
        return sut.create(project.id(), Repository.of(random(String.class), random(String.class), random(RepositorySource.class)));
    }
}
