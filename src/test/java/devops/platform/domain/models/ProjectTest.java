package devops.platform.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;

class ProjectTest {

    private String key;
    private String name;

    @BeforeEach
    void setup() {
        key = random(String.class);
        name = random(String.class);
    }

    @Test
    void of_shouldSetIdToNull() {
        // Act
        Project sut = Project.of(key, name);
        // Assert
        assertThat(sut.id()).isNull();
    }

    @Test
    void of_shouldSetKey() {
        // Act
        Project sut = Project.of(key, name);
        // Assert
        assertThat(sut.key()).isEqualTo(key);
    }

    @Test
    void of_shouldSetName() {
        // Act
        Project sut = Project.of(key, name);
        // Assert
        assertThat(sut.name()).isEqualTo(name);
    }
}
