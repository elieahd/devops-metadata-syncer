package devops.metadata.syncer.domain.services;

import devops.metadata.syncer.domain.exceptions.ProjectNotFoundException;
import devops.metadata.syncer.domain.exceptions.SourceNotFoundException;
import devops.metadata.syncer.domain.inbound.SyncProject;
import devops.metadata.syncer.domain.inbound.SyncRepository;
import devops.metadata.syncer.domain.models.Project;
import devops.metadata.syncer.domain.models.Repository;
import devops.metadata.syncer.domain.outbound.ProjectInventory;
import devops.metadata.syncer.domain.outbound.RepositoryInventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@DomainService
public class SyncProjectService implements SyncProject {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncProjectService.class);

    private final ProjectInventory projectInventory;
    private final RepositoryInventory repositoryInventory;
    private final SyncRepository syncRepository;

    public SyncProjectService(ProjectInventory projectInventory,
                              RepositoryInventory repositoryInventory,
                              SyncRepository syncRepository) {
        this.projectInventory = projectInventory;
        this.repositoryInventory = repositoryInventory;
        this.syncRepository = syncRepository;
    }

    @Override
    public void sync(String projectKey) throws ProjectNotFoundException, SourceNotFoundException {

        LOGGER.info("Synchronizing project '{}'...", projectKey);

        Project project = projectInventory.findByKey(projectKey)
                .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        List<Repository> repositories = repositoryInventory.findAllByProjectId(project.id());

        LOGGER.info("{} repositories found", repositories.size());

        for (int i = 0; i < repositories.size(); i++) {
            Repository repository = repositories.get(i);
            LOGGER.info("  → Synchronizing {} repository {}/{} : '{}/{}'...", repository.source().name(), i + 1, repositories.size(), repository.organization(), repository.name());
            syncRepository.sync(repository);
        }

    }

}
