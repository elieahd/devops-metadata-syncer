package devops.metadata.syncer.infrastructure.outbound.database;

import devops.metadata.syncer.domain.models.Project;
import devops.metadata.syncer.domain.outbound.ProjectInventory;
import devops.metadata.syncer.infrastructure.outbound.OutboundAdapter;
import devops.metadata.syncer.infrastructure.outbound.database.dao.ProjectDao;

import java.util.Optional;

@OutboundAdapter
public class ProjectInventoryDatabaseAdapter implements ProjectInventory {

    private final ProjectDao dao;

    public ProjectInventoryDatabaseAdapter(ProjectDao dao) {
        this.dao = dao;
    }

    @Override
    public Optional<Project> findByKey(String key) {
        Project project = dao.findByKey(key);
        return Optional.ofNullable(project);
    }

    @Override
    public Project create(Project project) {
        Long id = dao.create(project);
        return new Project(
                id,
                project.key(),
                project.name()
        );
    }
}
