package devops.platform.domain.outbound;

import devops.platform.domain.models.Pipeline;

import java.util.List;

public interface PipelineInventory {

    void deleteAllByRepositoryId(Long repositoryId);

    void insertAll(Long repositoryId,
                   List<Pipeline> pipelines);

    List<Pipeline> findAllByRepositoryId(Long repositoryId);
}
