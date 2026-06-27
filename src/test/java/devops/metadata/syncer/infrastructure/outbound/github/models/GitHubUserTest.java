package devops.metadata.syncer.infrastructure.outbound.github.models;

import org.junit.jupiter.api.Test;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;

class GitHubUserTest {

    @Test
    void isUser_shouldReturnTrue_whenTypeIsUser() {
        // Arrange
        GitHubUser sut = new GitHubUser(random(String.class), "User");
        // Act
        boolean isUser = sut.isUser();
        // Assert
        assertThat(isUser).isTrue();
    }

    @Test
    void isUser_shouldReturnFalse_whenTypeIsNotUser() {
        // Arrange
        GitHubUser sut = new GitHubUser(random(String.class), random(String.class));
        // Act
        boolean isUser = sut.isUser();
        // Assert
        assertThat(isUser).isFalse();
    }
}
