package devops.metadata.syncer.domain.models;

import devops.metadata.syncer.domain.models.randomizers.PipelineRunRandomizer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PipelineRunTest {

    private static Stream<Arguments> provideDuration() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime nowPlus5Min = now.plusMinutes(5);
        OffsetDateTime nowPlus10Min = now.plusMinutes(10);
        OffsetDateTime nowPlus1MinAnd30Sec = now.plusMinutes(1).plusSeconds(30);

        return Stream.of(
                Arguments.of(null, nowPlus5Min, Duration.ZERO),
                Arguments.of(now, null, Duration.ZERO),
                Arguments.of(null, null, Duration.ZERO),
                Arguments.of(now, nowPlus5Min, Duration.ofMinutes(5)),
                Arguments.of(now, nowPlus10Min, Duration.ofMinutes(10)),
                Arguments.of(now, nowPlus1MinAnd30Sec, Duration.ofMinutes(1).plusSeconds(30))
        );
    }

    @ParameterizedTest
    @MethodSource("provideDuration")
    void duration_shouldCalculate(OffsetDateTime startedAt,
                                  OffsetDateTime updatedAt,
                                  Duration expectedDuration) {
        // Arrange
        PipelineRun sut = PipelineRunRandomizer.builder()
                .startedAt(startedAt)
                .updatedAt(updatedAt)
                .build();
        // Act
        Duration duration = sut.duration();
        // Assert
        assertThat(duration).isEqualTo(expectedDuration);
    }

}
