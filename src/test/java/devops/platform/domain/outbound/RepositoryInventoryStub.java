package devops.platform.domain.outbound;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.Repository;
import devops.platform.domain.models.RepositorySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositoryInventoryStub implements RepositoryInventory {

    private final Map<Long, List<Repository>> repositoriesByProjectId;
    private final Map<String, List<Repository>> repositoriesByProjectKey;
    private final Map<Long, LocalDateTime> updatedSyncTimesByRepositoryId;

    public RepositoryInventoryStub() {
        this.repositoriesByProjectId = new HashMap<>();
        this.repositoriesByProjectKey = new HashMap<>();
        this.updatedSyncTimesByRepositoryId = new HashMap<>();
    }

    @Override
    public List<Repository> findAllByProjectId(Long projectId) {
        return repositoriesByProjectId.getOrDefault(projectId, new ArrayList<>());
    }

    @Override
    public List<Repository> findAllByProjectKey(String projectKey) {
        return repositoriesByProjectKey.getOrDefault(projectKey, new ArrayList<>());
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
        repositoriesByProjectKey
                .computeIfAbsent(projectId.toString(), _ -> new ArrayList<>())
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

    public Repository create(Project project, Repository repository) {
        create(project.id(), repository);
        repositoriesByProjectKey
                .computeIfAbsent(project.key(), _ -> new ArrayList<>())
                .add(repository);
        return repository;
    }
}