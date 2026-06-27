package devops.metadata.syncer.domain.outbound;

import devops.metadata.syncer.domain.models.Release;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReleaseInventoryStub implements ReleaseInventory {

    private final Map<Long, List<Release>> releasesByRepositoryId;

    public ReleaseInventoryStub() {
        this.releasesByRepositoryId = new HashMap<>();
    }

    @Override
    public void deleteAllByRepositoryId(Long repositoryId) {
        releasesByRepositoryId.remove(repositoryId);
    }

    @Override
    public void insertAll(Long repositoryId, List<Release> releases) {
        releasesByRepositoryId
                .computeIfAbsent(repositoryId, id -> new ArrayList<>())
                .addAll(releases);
    }

    @Override
    public List<Release> findAllByRepositoryId(Long repositoryId) {
        return releasesByRepositoryId.getOrDefault(repositoryId, List.of());
    }
}