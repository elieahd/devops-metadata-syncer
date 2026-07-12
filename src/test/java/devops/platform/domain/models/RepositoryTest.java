package devops.platform.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;

class RepositoryTest {

    private String organization;
    private String name;
    private RepositorySource source;

    @BeforeEach
    void setup() {
        organization = random(String.class);
        name = random(String.class);
        source = random(RepositorySource.class);
    }

    @Test
    void of_shouldSetIdToNull() {
        // Act
        Repository sut = Repository.of(organization, name, source);
        // Assert
        assertThat(sut.id()).isNull();
    }

    @Test
    void of_shouldSetOrganization() {
        // Act
        Repository sut = Repository.of(organization, name, source);
        // Assert
        assertThat(sut.organization()).isEqualTo(organization);
    }

    @Test
    void of_shouldSetName() {
        // Act
        Repository sut = Repository.of(organization, name, source);
        // Assert
        assertThat(sut.name()).isEqualTo(name);
    }

    @Test
    void of_shouldSetSource() {
        // Act
        Repository sut = Repository.of(organization, name, source);
        // Assert
        assertThat(sut.source()).isEqualTo(source);
    }

    @Test
    void of_shouldSetLastSyncTimeToNull() {
        // Act
        Repository sut = Repository.of(organization, name, source);
        // Assert
        assertThat(sut.lastSyncTime()).isNull();
    }
}
