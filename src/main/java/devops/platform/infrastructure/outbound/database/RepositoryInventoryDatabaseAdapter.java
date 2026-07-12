package devops.platform.infrastructure.outbound.database;

import devops.platform.domain.models.Repository;
import devops.platform.domain.models.RepositorySource;
import devops.platform.domain.outbound.RepositoryInventory;
import devops.platform.infrastructure.outbound.OutboundAdapter;
import devops.platform.infrastructure.outbound.database.dao.RepositoryDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@OutboundAdapter
public class RepositoryInventoryDatabaseAdapter implements RepositoryInventory {

    private final RepositoryDao dao;

    public RepositoryInventoryDatabaseAdapter(RepositoryDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Repository> findAllByProjectId(Long projectId) {
        return dao.findAllByProjectId(projectId);
    }

    @Override
    public List<Repository> findAllByProjectKey(String projectKey) {
        return dao.findAllByProjectKey(projectKey);
    }

    @Override
    public void updateLastSyncTime(Long repositoryId, LocalDateTime lastSyncTime) {
        dao.updateLastSyncTime(repositoryId, lastSyncTime);
    }

    @Override
    public Repository create(Long projectId, Repository repository) {
        Long id = dao.create(projectId, repository);
        return new Repository(
                id,
                repository.organization(),
                repository.name(),
                repository.source(),
                null
        );
    }

    @Override
    public Optional<Repository> findOneByOrganizationAndNameAndSource(String organization,
                                                                      String name,
                                                                      RepositorySource source) {
        Repository repository = dao.findOneByOrganizationAndNameAndSource(organization, name, source);
        return Optional.ofNullable(repository);
    }

}
