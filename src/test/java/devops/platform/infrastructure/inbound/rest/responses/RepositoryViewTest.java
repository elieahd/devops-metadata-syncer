package devops.platform.infrastructure.inbound.rest.responses;

import devops.platform.domain.models.Repository;
import devops.platform.domain.models.randomizers.RepositoryRandomizer;
import devops.platform.infrastructure.inbound.rest.responses.RepositoryView;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RepositoryViewTest {

    @Test
    void map_shouldReturnNull_whenRepositoryIsNull() {
        // Arrange
        Repository repository = null;
        // Act
        RepositoryView repositoryView = RepositoryView.map(repository);
        // Assert
        assertThat(repositoryView).isNull();
    }

    @Test
    void map_shouldMapOrganization() {
        // Arrange
        Repository repository = RepositoryRandomizer.random();
        // Act
        RepositoryView repositoryView = RepositoryView.map(repository);
        // Assert
        assertThat(repositoryView).isNotNull();
        assertThat(repositoryView.organization()).isEqualTo(repository.organization());
    }

    @Test
    void map_shouldMapName() {
        // Arrange
        Repository repository = RepositoryRandomizer.random();
        // Act
        RepositoryView repositoryView = RepositoryView.map(repository);
        // Assert
        assertThat(repositoryView).isNotNull();
        assertThat(repositoryView.name()).isEqualTo(repository.name());
    }

    @Test
    void map_shouldMapSource() {
        // Arrange
        Repository repository = RepositoryRandomizer.random();
        // Act
        RepositoryView repositoryView = RepositoryView.map(repository);
        // Assert
        assertThat(repositoryView).isNotNull();
        assertThat(repositoryView.source()).isEqualTo(repository.source());
    }

    @Test
    void map_shouldMapLastSyncTime() {
        // Arrange
        Repository repository = RepositoryRandomizer.random();
        // Act
        RepositoryView repositoryView = RepositoryView.map(repository);
        // Assert
        assertThat(repositoryView).isNotNull();
        assertThat(repositoryView.lastSyncTime()).isEqualTo(repository.lastSyncTime());
    }
}
