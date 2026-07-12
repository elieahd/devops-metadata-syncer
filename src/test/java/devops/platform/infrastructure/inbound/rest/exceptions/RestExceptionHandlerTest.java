package devops.platform.infrastructure.inbound.rest.exceptions;

import devops.platform.domain.exceptions.ProjectNotFoundException;
import devops.platform.domain.exceptions.RepositoryNotFoundException;
import devops.platform.domain.exceptions.SourceNotFoundException;
import devops.platform.domain.models.RepositorySource;
import devops.platform.infrastructure.inbound.rest.exceptions.RestExceptionHandler;
import devops.platform.infrastructure.inbound.rest.responses.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;

class RestExceptionHandlerTest {

    private RestExceptionHandler sut;

    @BeforeEach
    void setup() {
        sut = new RestExceptionHandler();
    }

    private static Stream<Arguments> notFoundExceptions() {
        return Stream.of(
                Arguments.of(new ProjectNotFoundException(random(String.class))),
                Arguments.of(new RepositoryNotFoundException(random(String.class), random(String.class), random(RepositorySource.class))),
                Arguments.of(new SourceNotFoundException(random(String.class)))
        );
    }

    @ParameterizedTest
    @MethodSource("notFoundExceptions")
    void handleNotFoundException_shouldReturnErrorResponseInBody(Exception exception) {
        // Act
        ResponseEntity<ErrorResponse> response = sut.handleNotFoundException(exception);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().errorCode()).isEqualTo("NOT_FOUND");
        assertThat(response.getBody().errorMessage()).isEqualTo(exception.getMessage());
    }
}
