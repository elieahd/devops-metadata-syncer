package devops.metadata.syncer.infrastructure.outbound.database;

import devops.metadata.syncer.domain.models.Release;
import devops.metadata.syncer.domain.outbound.ReleaseInventory;
import devops.metadata.syncer.infrastructure.outbound.OutboundAdapter;
import devops.metadata.syncer.infrastructure.outbound.database.dao.ReleaseDao;

import java.util.List;

@OutboundAdapter
public class ReleaseInventoryDatabaseAdapter implements ReleaseInventory {

    private final ReleaseDao dao;

    public ReleaseInventoryDatabaseAdapter(ReleaseDao dao) {
        this.dao = dao;
    }

    @Override
    public void deleteAllByRepositoryId(Long repositoryId) {
        dao.deleteAllByRepositoryId(repositoryId);
    }

    @Override
    public void insertAll(Long repositoryId, List<Release> releases) {
        if (releases != null && !releases.isEmpty()) {
            dao.insertAll(repositoryId, releases);
        }
    }

    @Override
    public List<Release> findAllByRepositoryId(Long repositoryId) {
        return dao.findAllByRepositoryId(repositoryId);
    }
}
