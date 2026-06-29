package devops.metadata.syncer.domain.outbound;

import devops.metadata.syncer.domain.models.Repository;
import devops.metadata.syncer.domain.models.RepositorySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositoryInventoryStub implements RepositoryInventory {

    private final Map<Long, List<Repository>> repositoriesByProjectId;
    private final Map<Long, LocalDateTime> updatedSyncTimesByRepositoryId = new HashMap<>();

    public RepositoryInventoryStub() {
        this.repositoriesByProjectId = new HashMap<>();
    }

    @Override
    public List<Repository> findAllByProjectId(Long projectId) {
        return repositoriesByProjectId.getOrDefault(projectId, new ArrayList<>());
    }

    @Override
    public void updateLastSyncTime(Long repositoryId, LocalDateTime lastSyncTime) {
        updatedSyncTimesByRepositoryId.put(repositoryId, lastSyncTime);
    }

    @Override
    public Repository create(Long projectId, Repository repository) {
        repositoriesByProjectId
                .computeIfAbsent(projectId, _ -> new ArrayList<>())
                .add(repository);
        return repository;
    }

    @Override
    public Optional<Repository> findOneByOrganizationAndNameAndSource(String organization,
                                                                      String name,
                                                                      RepositorySource source) {
        return repositoriesByProjectId.values()
                .stream()
                .flatMap(List::stream)
                .filter(repository -> organization.equals(repository.organization()) && name.equals(repository.name()) && source.equals(repository.source()))
                .findFirst();
    }

    public LocalDateTime getLastSyncTime(Repository repository) {
        return updatedSyncTimesByRepositoryId.getOrDefault(repository.id(), null);
    }
}