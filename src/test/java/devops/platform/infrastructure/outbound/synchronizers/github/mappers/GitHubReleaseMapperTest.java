package devops.platform.infrastructure.outbound.synchronizers.github.mappers;

import devops.platform.domain.models.Release;
import devops.platform.infrastructure.outbound.github.models.GitHubRelease;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static devops.platform.infrastructure.outbound.github.models.GitHubModelRandomizer.aGitHubRelease;
import static org.assertj.core.api.Assertions.assertThat;

class GitHubReleaseMapperTest {

    private GitHubReleaseMapper sut;

    @BeforeEach
    void setUp() {
        sut = new GitHubReleaseMapper();
    }

    @Test
    void map_shouldReturnNull_WhenInputIsNull() {
        // Arrange
        GitHubRelease gitHubRelease = null;
        // Act
        Release release = sut.map(gitHubRelease);
        // Assert
        assertThat(release).isNull();
    }

    @Test
    void map_shouldMapName() {
        // Arrange
        GitHubRelease gitHubRelease = aGitHubRelease();
        // Act
        Release release = sut.map(gitHubRelease);
        // Assert
        assertThat(release.name()).isEqualTo(gitHubRelease.name());
    }

    @Test
    void map_shouldMapTagName() {
        // Arrange
        GitHubRelease gitHubRelease = aGitHubRelease();
        // Act
        Release release = sut.map(gitHubRelease);
        // Assert
        assertThat(release.tagName()).isEqualTo(gitHubRelease.tagName());
    }

    @Test
    void map_shouldPublishedAt() {
        // Arrange
        GitHubRelease gitHubRelease = aGitHubRelease();
        // Act
        Release release = sut.map(gitHubRelease);
        // Assert
        assertThat(release.publishedAt()).isEqualTo(gitHubRelease.publishedAt());
    }

}
