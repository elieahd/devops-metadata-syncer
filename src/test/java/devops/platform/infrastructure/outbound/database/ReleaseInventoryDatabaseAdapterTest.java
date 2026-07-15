package devops.platform.infrastructure.outbound.database;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.Release;
import devops.platform.domain.models.Repository;
import devops.platform.domain.models.assertions.ReleaseAssertions;
import devops.platform.domain.models.randomizers.ReleaseRandomizer;
import devops.platform.infrastructure.OutboundDatabaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReleaseInventoryDatabaseAdapterTest extends OutboundDatabaseIntegrationTest {

    @Autowired
    private ReleaseInventoryDatabaseAdapter sut;

    @Test
    void insertAll_shouldInsertAllReleasesForAGivenRepository() {
        // Arrange
        Project project = createProject();
        Repository repository = createRepository(project);
        Repository anotherRepository = createRepository(project);
        Release release1 = ReleaseRandomizer.random();
        Release release2 = ReleaseRandomizer.random();
        sut.insertAll(repository.id(), List.of(release1, release2));
        Release release3 = ReleaseRandomizer.random();
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
        Release release1 = ReleaseRandomizer.random();
        Release release2 = ReleaseRandomizer.random();
        sut.insertAll(repository.id(), List.of(release1, release2));
        Release release3 = ReleaseRandomizer.random();
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
}
