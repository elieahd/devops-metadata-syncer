package devops.platform.infrastructure.outbound.database;

import devops.platform.domain.models.Release;
import devops.platform.domain.outbound.ReleaseInventory;
import devops.platform.infrastructure.outbound.OutboundAdapter;
import devops.platform.infrastructure.outbound.database.dao.ReleaseDao;

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
