package devops.metadata.syncer.domain.outbound;

import devops.metadata.syncer.domain.models.Repository;
import devops.metadata.syncer.domain.models.RepositorySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RepositoryInventory {

    List<Repository> findAllByProjectId(Long projectId);

    void updateLastSyncTime(Long repositoryId, LocalDateTime lastSyncTime);

    Repository create(Long projectId, Repository repository);

    Optional<Repository> findOneByOrganizationAndNameAndSource(String organization,
                                                               String name,
                                                               RepositorySource source);

}
