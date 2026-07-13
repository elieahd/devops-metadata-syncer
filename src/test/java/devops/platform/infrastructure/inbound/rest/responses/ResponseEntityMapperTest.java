package devops.platform.infrastructure.inbound.rest.responses;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseEntityMapperTest {

    @Test
    void ok_shouldReturnNoContent_whenInputIsNull() {
        // Arrange
        List<String> input = null;
        Function<String, Integer> mapper = String::length;
        // Act
        ResponseEntity<List<Integer>> response = ResponseEntityMapper.ok(input, mapper);
        // Assert
        ResponseEntityAssertions.assertThat(response)
                .is204()
                .hasEmptyBody();
    }

    @Test
    void ok_shouldReturnNoContent_whenInputIsEmpty() {
        // Arrange
        List<String> input = List.of();
        Function<String, Integer> mapper = String::length;
        // Act
        ResponseEntity<List<Integer>> response = ResponseEntityMapper.ok(input, mapper);
        // Assert
        ResponseEntityAssertions.assertThat(response)
                .is204()
                .hasEmptyBody();
    }

    @Test
    void ok_shouldReturnMappedList() {
        // Arrange
        List<String> input = List.of("a", "bb", "ccc");
        Function<String, Integer> mapper = String::length;
        // Act
        ResponseEntity<List<Integer>> response = ResponseEntityMapper.ok(input, mapper);
        // Assert
        ResponseEntityAssertions.assertThat(response).is200();
        assertThat(response.getBody())
                .isNotNull()
                .containsExactly(1, 2, 3);
    }
}
