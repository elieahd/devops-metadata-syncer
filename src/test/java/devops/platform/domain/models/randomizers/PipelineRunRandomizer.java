package devops.platform.domain.models.randomizers;

import com.devt.randomizer.RandomizerUtils;
import devops.platform.domain.models.PipelineRun;

import java.time.OffsetDateTime;

public class PipelineRunRandomizer {

    private final boolean success;
    private final OffsetDateTime createdAt;
    private OffsetDateTime startedAt;
    private OffsetDateTime updatedAt;

    public PipelineRunRandomizer() {
        this.success = RandomizerUtils.random(Boolean.class);
        this.startedAt = RandomizerUtils.random(OffsetDateTime.class);
        this.createdAt = RandomizerUtils.random(OffsetDateTime.class);
        this.updatedAt = RandomizerUtils.random(OffsetDateTime.class);
    }

    public static PipelineRunRandomizer builder() {
        return new PipelineRunRandomizer();
    }

    public static PipelineRun random() {
        return builder().build();
    }

    public PipelineRunRandomizer startedAt(OffsetDateTime startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    public PipelineRunRandomizer updatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public PipelineRun build() {
        return new PipelineRun(
                success,
                startedAt,
                createdAt,
                updatedAt
        );
    }

}