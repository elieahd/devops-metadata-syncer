package devops.metadata.syncer.infrastructure.outbound.github;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class GitHubClientBuilderTest {

    @Test
    void build_shouldBuildClientWithDefaultUrl() {
        // Arrange
        String token = "my-token";
        // Act
        GitHubClient client = GitHubClientBuilder.builder()
                .token(token)
                .build();
        // Assert
        assertThat(client)
                .isNotNull()
                .isInstanceOf(GitHubClientAdapter.class);
    }

    @Test
    void build_shouldBuildClientWithCustomUrl() {
        // Arrange
        String token = "my-token";
        String url = "https://github.example.com/api";
        // Act
        GitHubClient client = GitHubClientBuilder.builder()
                .token(token)
                .url(url)
                .build();
        // Assert
        assertThat(client)
                .isNotNull()
                .isInstanceOf(GitHubClientAdapter.class);
    }

    @Test
    void build_shouldThrowException_whenTokenIsNull() {
        // Arrange
        GitHubClientBuilder builder = GitHubClientBuilder.builder();
        // Act
        Throwable thrown = catchThrowable(builder::build);
        // Assert
        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("GitHub Client 'token' must not be null");
    }

    @Test
    void build_shouldThrowException_whenUrlIsNull() {
        // Arrange
        String token = "my-token";
        GitHubClientBuilder builder = GitHubClientBuilder.builder()
                .token(token)
                .url(null);
        // Act
        Throwable thrown = catchThrowable(builder::build);
        // Assert
        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("GitHub Client 'url' must not be null");
    }
}