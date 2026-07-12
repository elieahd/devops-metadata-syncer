package devops.platform.domain.outbound;

import devops.platform.domain.models.Repository;
import devops.platform.domain.models.RepositorySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RepositoryInventory {

    List<Repository> findAllByProjectId(Long projectId);

    List<Repository> findAllByProjectKey(String projectKey);

    void updateLastSyncTime(Long repositoryId, LocalDateTime lastSyncTime);

    Repository create(Long projectId, Repository repository);

    Optional<Repository> findOneByOrganizationAndNameAndSource(String organization,
                                                               String name,
                                                               RepositorySource source);

}
