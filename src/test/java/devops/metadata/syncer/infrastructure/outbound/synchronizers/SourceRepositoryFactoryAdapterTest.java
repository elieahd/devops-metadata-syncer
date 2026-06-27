package devops.metadata.syncer.infrastructure.outbound.synchronizers;

import devops.metadata.syncer.domain.exceptions.SourceNotFoundException;
import devops.metadata.syncer.domain.models.RepositorySource;
import devops.metadata.syncer.domain.outbound.SourceRepositoryInventory;
import devops.metadata.syncer.infrastructure.outbound.synchronizers.github.EnterpriseGitHubRepositoryInventory;
import devops.metadata.syncer.infrastructure.outbound.synchronizers.github.GithubRepositoryInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class SourceRepositoryFactoryAdapterTest {

    private SourceRepositoryFactoryAdapter sut;

    @BeforeEach
    void setUp() {
        GithubRepositoryInventory github = new GithubRepositoryInventory(null, null, null, null, null);
        EnterpriseGitHubRepositoryInventory enterpriseGithub = new EnterpriseGitHubRepositoryInventory(null, null, null, null, null);
        sut = new SourceRepositoryFactoryAdapter(github, enterpriseGithub);
    }

    @Test
    void of_shouldReturnGithubInventory_whenSourceIsGithub() throws SourceNotFoundException {
        // Arrange
        RepositorySource source = RepositorySource.GITHUB;
        // Act
        SourceRepositoryInventory result = sut.of(source);
        // Assert
        assertThat(result).isInstanceOf(GithubRepositoryInventory.class);
    }

    @Test
    void of_shouldReturnEnterpriseGithubInventory_whenSourceIsEnterpriseGithub() throws SourceNotFoundException {
        // Arrange
        RepositorySource source = RepositorySource.ENTERPRISE_GITHUB;
        // Act
        SourceRepositoryInventory result = sut.of(source);
        // Assert
        assertThat(result).isInstanceOf(EnterpriseGitHubRepositoryInventory.class);
    }

    @Test
    void of_shouldThrowSourceNotFoundException_whenSourceIsUnknown() {
        // Arrange
        RepositorySource source = RepositorySource.UNKNOWN;
        // Act
        Throwable thrown = catchThrowable(() -> sut.of(source));
        // Assert
        assertThat(thrown)
                .isInstanceOf(SourceNotFoundException.class)
                .hasMessage("No synchronizer found for UNKNOWN");
    }

    @Test
    void of_shouldThrowSourceNullPointerException_whenSourceIsNull() {
        // Arrange
        RepositorySource source = null;
        // Act
        Throwable thrown = catchThrowable(() -> sut.of(source));
        // Assert
        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Source shouldn't be null");
    }
}
