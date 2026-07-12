package devops.platform.domain.services;

import devops.platform.domain.exceptions.ProjectNotFoundException;
import devops.platform.domain.inbound.GetRepositories;
import devops.platform.domain.models.Repository;
import devops.platform.domain.outbound.ProjectInventory;
import devops.platform.domain.outbound.RepositoryInventory;

import java.util.List;

@DomainService
public class RepositoryService implements GetRepositories {

    private final RepositoryInventory repositoryInventory;
    private final ProjectInventory projectInventory;

    public RepositoryService(RepositoryInventory repositoryInventory,
                             ProjectInventory projectInventory) {
        this.repositoryInventory = repositoryInventory;
        this.projectInventory = projectInventory;
    }

    @Override
    public List<Repository> getAllByProjectKey(String projectKey) throws ProjectNotFoundException {
        if (!projectInventory.existsByKey(projectKey)) {
            throw new ProjectNotFoundException(projectKey);
        }
        return repositoryInventory.findAllByProjectKey(projectKey);
    }

}
