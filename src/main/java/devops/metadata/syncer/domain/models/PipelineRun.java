package devops.metadata.syncer.domain.models;

import java.time.Duration;
import java.time.OffsetDateTime;

public record PipelineRun(boolean success,
                          OffsetDateTime startedAt,
                          OffsetDateTime createdAt,
                          OffsetDateTime updatedAt,
                          Duration duration) {

    public PipelineRun(boolean success,
                       OffsetDateTime startedAt,
                       OffsetDateTime createdAt,
                       OffsetDateTime updatedAt) {
        this(
                success,
                startedAt,
                createdAt,
                updatedAt,
                computeDuration(startedAt, updatedAt)
        );
    }

    private static Duration computeDuration(OffsetDateTime startedAt,
                                            OffsetDateTime updatedAt) {
        return startedAt == null || updatedAt == null
                ? Duration.ZERO
                : Duration.between(startedAt, updatedAt);
    }
}
