package devops.platform.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;

class OrganizationTest {

    private String acronym;
    private String name;
    private String description;

    @BeforeEach
    void setup() {
        acronym = random(String.class);
        name = random(String.class);
        description = random(String.class);
    }

    @Test
    void of_shouldSetIdToNull() {
        // Act
        Organization sut = Organization.of(acronym, name, description);
        // Assert
        assertThat(sut.id()).isNull();
    }

    @Test
    void of_shouldSetAcronym() {
        // Act
        Organization sut = Organization.of(acronym, name, description);
        // Assert
        assertThat(sut.acronym()).isEqualTo(acronym);
    }

    @Test
    void of_shouldSetName() {
        // Act
        Organization sut = Organization.of(acronym, name, description);
        // Assert
        assertThat(sut.name()).isEqualTo(name);
    }

    @Test
    void of_shouldSetDescription() {
        // Act
        Organization sut = Organization.of(acronym, name, description);
        // Assert
        assertThat(sut.description()).isEqualTo(description);
    }
}
