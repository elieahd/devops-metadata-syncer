package devops.platform.domain.models.randomizers;

import com.devt.randomizer.RandomizerUtils;
import devops.platform.domain.models.Pipeline;
import devops.platform.domain.models.PipelineRun;

import java.util.List;
import java.util.stream.IntStream;

public class PipelineRandomizer {

    private String name;
    private String sourceId;
    private List<PipelineRun> runs;

    public PipelineRandomizer() {
        this.name = RandomizerUtils.random(String.class);
        this.sourceId = RandomizerUtils.random(String.class);
        this.runs = IntStream.range(0, 5).mapToObj(_ -> PipelineRunRandomizer.random()).toList();
    }

    public static PipelineRandomizer builder() {
        return new PipelineRandomizer();
    }

    public static Pipeline random() {
        return builder().build();
    }

    public PipelineRandomizer runs(List<PipelineRun> runs) {
        this.runs = runs;
        return this;
    }

    public Pipeline build() {
        return new Pipeline(name, sourceId, runs);
    }

}
