package devops.platform.domain.outbound;

import devops.platform.domain.models.Pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PipelineInventoryStub implements PipelineInventory {

    private final Map<Long, List<Pipeline>> pipelinesByRepositoryId;

    public PipelineInventoryStub() {
        this.pipelinesByRepositoryId = new HashMap<>();
    }

    @Override
    public void deleteAllByRepositoryId(Long repositoryId) {
        pipelinesByRepositoryId.remove(repositoryId);
    }

    @Override
    public void insertAll(Long repositoryId, List<Pipeline> pipelines) {
        pipelinesByRepositoryId
                .computeIfAbsent(repositoryId, id -> new ArrayList<>())
                .addAll(pipelines);
    }

    @Override
    public List<Pipeline> findAllByRepositoryId(Long repositoryId) {
        return pipelinesByRepositoryId.getOrDefault(repositoryId, List.of());
    }
}