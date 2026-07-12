package devops.platform.infrastructure.outbound.database;

import devops.platform.domain.models.Project;
import devops.platform.domain.outbound.ProjectInventory;
import devops.platform.infrastructure.outbound.OutboundAdapter;
import devops.platform.infrastructure.outbound.database.dao.ProjectDao;

import java.util.List;
import java.util.Optional;

@OutboundAdapter
public class ProjectInventoryDatabaseAdapter implements ProjectInventory {

    private final ProjectDao dao;

    public ProjectInventoryDatabaseAdapter(ProjectDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Project> findAll() {
        return dao.findAll();
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

    @Override
    public boolean existsByKey(String projectKey) {
        return dao.existsByKey(projectKey);
    }
}
