package devops.metadata.syncer.infrastructure.inbound.cli.helper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationArgumentsHelperTest {

    @Test
    void getArg_shouldReturnFirstValue_whenOptionExistsWithValues() {
        // Arrange
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of(
                        "name", List.of("Alice", "Bob")
                )
        );
        ApplicationArgumentsHelper helper = ApplicationArgumentsHelper.of(args);
        // Act
        Optional<String> result = helper.getArg("name");
        // Assert
        assertThat(result).contains("Alice");
    }

    @Test
    void getArg_shouldReturnEmpty_whenOptionDoesNotExist() {
        // Arrange
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of(
                        "other", List.of("value")
                )
        );
        ApplicationArgumentsHelper helper = ApplicationArgumentsHelper.of(args);
        // Act
        Optional<String> result = helper.getArg("name");
        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void getArg_shouldReturnEmpty_whenOptionExistsButValuesIsEmpty() {
        // Arrange
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of(
                        "name", List.of()
                )
        );
        ApplicationArgumentsHelper helper = ApplicationArgumentsHelper.of(args);
        // Act
        Optional<String> result = helper.getArg("name");
        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void getArgs_shouldReturnAllValues_whenOptionExistsWithValues() {
        // Arrange
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of(
                        "name", List.of("Alice", "Bob")
                )
        );
        ApplicationArgumentsHelper helper = ApplicationArgumentsHelper.of(args);
        // Act
        Optional<List<String>> result = helper.getArgs("name");
        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).containsExactly("Alice", "Bob");
    }

    @Test
    void getArgs_shouldReturnEmpty_whenOptionDoesNotExist() {
        // Arrange
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of(
                        "other", List.of("value")
                )
        );
        ApplicationArgumentsHelper helper = ApplicationArgumentsHelper.of(args);

        // Act
        Optional<List<String>> result = helper.getArgs("name");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void getArgs_shouldReturnEmpty_whenOptionExistsButValuesIsEmpty() {
        // Arrange
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of(
                        "name", List.of()
                )
        );
        ApplicationArgumentsHelper helper = ApplicationArgumentsHelper.of(args);

        // Act
        Optional<List<String>> result = helper.getArgs("name");

        // Assert
        assertThat(result).isEmpty();
    }

}