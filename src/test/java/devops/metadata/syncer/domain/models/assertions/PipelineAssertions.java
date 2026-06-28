package devops.metadata.syncer.domain.models.assertions;

import devops.metadata.syncer.domain.models.Pipeline;
import devops.metadata.syncer.domain.models.PipelineRun;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.List;

public class PipelineAssertions extends AbstractAssert<PipelineAssertions, Pipeline> {

    public PipelineAssertions(Pipeline actual) {
        super(actual, PipelineAssertions.class);
    }

    public static PipelineAssertions assertThat(Pipeline actual) {
        return new PipelineAssertions(actual);
    }

    public PipelineAssertions isEqualTo(Pipeline expected) {

        isNotNull();

        Assertions.assertThat(actual.name()).isEqualTo(expected.name());
        Assertions.assertThat(actual.sourceId()).isEqualTo(expected.sourceId());

        if (isNullOrEmpty(actual.runs()) && isNullOrEmpty(expected.runs())) {
            return this;
        }

        Assertions.assertThat(actual.runs())
                .isNotNull()
                .isNotEmpty()
                .hasSameSizeAs(expected.runs());

        for (int i = 0; i < expected.runs().size(); i++) {
            PipelineRun pipelineRun = actual.runs().get(i);
            PipelineRun expectedPipelineRun = expected.runs().get(i);
            PipelineRunAssertions.assertThat(pipelineRun).isEqualTo(expectedPipelineRun);
        }

        return this;
    }

    private boolean isNullOrEmpty(List<PipelineRun> runs) {
        return runs == null || runs.isEmpty();
    }
}
