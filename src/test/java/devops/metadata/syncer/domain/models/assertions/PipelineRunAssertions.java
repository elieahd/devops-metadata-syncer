package devops.metadata.syncer.domain.models.assertions;

import devops.metadata.syncer.domain.models.PipelineRun;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.time.Duration;

public class PipelineRunAssertions extends AbstractAssert<PipelineRunAssertions, PipelineRun> {

    public PipelineRunAssertions(PipelineRun actual) {
        super(actual, PipelineRunAssertions.class);
    }

    public static PipelineRunAssertions assertThat(PipelineRun actual) {
        return new PipelineRunAssertions(actual);
    }

    public PipelineRunAssertions isEqualTo(PipelineRun expected) {
        isNotNull();
        Assertions.assertThat(actual.success()).isEqualTo(expected.success());
        Assertions.assertThat(actual.startedAt()).isEqualTo(expected.startedAt());
        Assertions.assertThat(actual.createdAt()).isEqualTo(expected.createdAt());
        Assertions.assertThat(actual.updatedAt()).isEqualTo(expected.updatedAt());
        Assertions.assertThat(actual.duration()).isCloseTo(expected.duration(), Duration.ofSeconds(1));
        return this;
    }
}
