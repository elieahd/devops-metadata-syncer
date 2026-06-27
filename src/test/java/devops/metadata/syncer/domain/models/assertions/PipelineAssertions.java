package devops.metadata.syncer.domain.models.assertions;

import devops.metadata.syncer.domain.models.Pipeline;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

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
        if ((actual.runs() == null || actual.runs().isEmpty())
                && (expected.runs() == null || expected.runs().isEmpty())) {
            return this;
        }
        for (int i = 0; i < expected.runs().size(); i++) {
            PipelineRunAssertions.assertThat(actual.runs().get(i)).isEqualTo(expected.runs().get(i));
        }
        return this;
    }
}
