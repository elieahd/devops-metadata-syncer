package devops.platform.domain.services;

import devops.platform.domain.exceptions.RepositoryNotFoundException;
import devops.platform.domain.exceptions.SourceNotFoundException;
import devops.platform.domain.inbound.SyncRepository;
import devops.platform.domain.models.Repository;
import devops.platform.domain.models.RepositorySource;

import java.util.HashSet;
import java.util.Set;

public class SyncRepositoryStub implements SyncRepository {

    private final Set<Long> syncedRepositoryIds;
    private final Set<String> syncedRepositories;

    public SyncRepositoryStub() {
        this.syncedRepositoryIds = new HashSet<>();
        this.syncedRepositories = new HashSet<>();
    }

    @Override
    public void sync(String organization, String repositoryName, RepositorySource repositorySource) throws RepositoryNotFoundException, SourceNotFoundException {
        syncedRepositories.add(key(organization, repositoryName, repositorySource));
    }

    @Override
    public void sync(Repository repository) throws SourceNotFoundException {
        syncedRepositoryIds.add(repository.id());
    }

    public boolean anySynced() {
        return !syncedRepositoryIds.isEmpty() || !syncedRepositories.isEmpty();
    }

    public boolean hasSynced(Long repositoryId) {
        return syncedRepositoryIds.contains(repositoryId);
    }

    public boolean hasSynced(String organization, String repositoryName, RepositorySource repositorySource) {
        String key = key(organization, repositoryName, repositorySource);
        return syncedRepositories.contains(key);
    }

    private String key(String organization, String repositoryName, RepositorySource repositorySource) {
        return "%s/%s/%s".formatted(organization, repositoryName, repositorySource);
    }

}
