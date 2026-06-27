package devops.metadata.syncer.infrastructure.outbound.database;

import devops.metadata.syncer.domain.models.Project;
import devops.metadata.syncer.domain.models.Release;
import devops.metadata.syncer.domain.models.Repository;
import devops.metadata.syncer.domain.models.RepositorySource;
import devops.metadata.syncer.domain.models.assertions.ReleaseAssertions;
import devops.metadata.syncer.infrastructure.OutboundDatabaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static devops.metadata.syncer.domain.models.ModelRandomizer.aRelease;
import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;

class ReleaseInventoryDatabaseAdapterTest extends OutboundDatabaseIntegrationTest {

    @Autowired
    private ProjectInventoryDatabaseAdapter projectInventory;

    @Autowired
    private RepositoryInventoryDatabaseAdapter repositoryInventory;

    @Autowired
    private ReleaseInventoryDatabaseAdapter sut;

    @Test
    void insertAll_shouldInsertAllReleasesForAGivenRepository() {
        // Arrange
        Project project = createProject();
        Repository repository = createRepository(project);
        Repository anotherRepository = createRepository(project);
        Release release1 = aRelease();
        Release release2 = aRelease();
        sut.insertAll(repository.id(), List.of(release1, release2));
        Release release3 = aRelease();
        sut.insertAll(anotherRepository.id(), List.of(release3));
        // Act
        List<Release> releases = sut.findAllByRepositoryId(repository.id());
        // Assert
        assertThat(releases)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        ReleaseAssertions.assertThat(releases.getFirst()).isEqualTo(release1);
        ReleaseAssertions.assertThat(releases.get(1)).isEqualTo(release2);
    }

    @Test
    void insertAll_shouldNotThrowException_whenReleasesIsEmpty() {
        // Arrange
        Project project = createProject();
        Repository repository = createRepository(project);
        List<Release> releases = new ArrayList<>();
        // Act
        sut.insertAll(repository.id(), releases);
    }

    @Test
    void insertAll_shouldNotThrowException_whenReleasesIsNull() {
        // Arrange
        Project project = createProject();
        Repository repository = createRepository(project);
        List<Release> releases = null;
        // Act
        sut.insertAll(repository.id(), releases);
    }

    @Test
    void deleteAllByRepositoryId_shouldDeleteAllReleasesForAGivenRepository() {
        // Arrange
        Project project = createProject();
        Repository repository = createRepository(project);
        Repository anotherRepository = createRepository(project);
        Release release1 = aRelease();
        Release release2 = aRelease();
        sut.insertAll(repository.id(), List.of(release1, release2));
        Release release3 = aRelease();
        sut.insertAll(anotherRepository.id(), List.of(release3));
        // Act
        sut.deleteAllByRepositoryId(repository.id());
        // Assert
        List<Release> repositoryReleases = sut.findAllByRepositoryId(repository.id());
        assertThat(repositoryReleases)
                .isNotNull()
                .isEmpty();
        List<Release> anotherRepositoryReleases = sut.findAllByRepositoryId(anotherRepository.id());
        assertThat(anotherRepositoryReleases)
                .isNotNull()
                .isNotEmpty();
    }

    private Project createProject() {
        Project project = Project.of(random(String.class), random(String.class));
        return projectInventory.create(project);
    }

    private Repository createRepository(Project project) {
        Repository repository = Repository.of(random(String.class), random(String.class), random(RepositorySource.class));
        return repositoryInventory.create(project.id(), repository);
    }
}
