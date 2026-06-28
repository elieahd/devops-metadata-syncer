package devops.metadata.syncer.infrastructure.inbound.cli.helper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationArgumentsHelperTest {

    @Test
    void getArg_shouldReturnFirstValue_whenOptionExistsWithValues() {
        // Arrange
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of("name", List.of("Alice", "Bob"))
        );
        ApplicationArgumentsHelper sut = ApplicationArgumentsHelper.of(args);
        // Act
        Optional<String> result = sut.getArg("name");
        // Assert
        assertThat(result).contains("Alice");
    }

    @Test
    void getArg_shouldReturnEmpty_whenOptionDoesNotExist() {
        // Arrange
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of("other", List.of("value"))
        );
        ApplicationArgumentsHelper sut = ApplicationArgumentsHelper.of(args);
        // Act
        Optional<String> result = sut.getArg("name");
        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void getArg_shouldReturnEmpty_whenOptionExistsButValuesIsEmpty() {
        // Arrange
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of("name", List.of())
        );
        ApplicationArgumentsHelper sut = ApplicationArgumentsHelper.of(args);
        // Act
        Optional<String> result = sut.getArg("name");
        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void getArg_shouldReturnEmpty_whenOptionExistsButValuesIsNull() {
        // Arrange
        Map<String, List<String>> options = new HashMap<>();
        options.put("name", null);
        ApplicationArguments args = new ApplicationArgumentsStub(options);
        ApplicationArgumentsHelper sut = ApplicationArgumentsHelper.of(args);
        // Act
        Optional<String> result = sut.getArg("name");
        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void getArg_shouldReturnEmpty_whenFirstValueIsNull() {
        // Arrange
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of("name", Arrays.asList(null, "Bob"))
        );
        ApplicationArgumentsHelper sut = ApplicationArgumentsHelper.of(args);
        // Act
        Optional<String> result = sut.getArg("name");
        // Assert
        assertThat(result).isEmpty();
    }

}