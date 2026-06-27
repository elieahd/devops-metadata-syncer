package devops.metadata.syncer.domain.outbound;

import devops.metadata.syncer.domain.models.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface RepositoryInventory {

    List<Repository> findAllByProjectId(Long projectId);

    void updateLastSyncTime(Long repositoryId, LocalDateTime lastSyncTime);

    Repository create(Long projectId, Repository repository);
}
