package devops.platform.domain.models;

import java.util.List;

public record Pipeline(String name,
                       String sourceId,
                       List<PipelineRun> runs) {

}
