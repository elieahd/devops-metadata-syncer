package devops.metadata.syncer.domain.outbound;

import devops.metadata.syncer.domain.models.Pipeline;

import java.util.List;

public interface PipelineInventory {

    void deleteAllByRepositoryId(Long repositoryId);

    void insertAll(Long repositoryId,
                   List<Pipeline> pipelines);

    List<Pipeline> findAllByRepositoryId(Long repositoryId);
}
