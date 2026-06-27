package devops.metadata.syncer.domain.services;

import devops.metadata.syncer.domain.exceptions.SourceNotFoundException;
import devops.metadata.syncer.domain.inbound.SyncRepository;
import devops.metadata.syncer.domain.models.Repository;

import java.util.HashSet;
import java.util.Set;

public class SyncRepositoryStub implements SyncRepository {

    private final Set<Long> syncedRepositories;

    public SyncRepositoryStub() {
        this.syncedRepositories = new HashSet<>();
    }

    @Override
    public void sync(Repository repository) throws SourceNotFoundException {
        syncedRepositories.add(repository.id());
    }

    public boolean anySynced() {
        return !syncedRepositories.isEmpty();
    }

    public boolean hasSynced(Long repositoryId) {
        return syncedRepositories.contains(repositoryId);
    }

}
