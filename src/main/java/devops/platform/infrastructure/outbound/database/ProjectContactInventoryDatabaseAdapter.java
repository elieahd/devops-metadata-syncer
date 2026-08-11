package devops.platform.infrastructure.outbound.database;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.ProjectContact;
import devops.platform.domain.outbound.ProjectContactInventory;
import devops.platform.infrastructure.outbound.OutboundAdapter;
import devops.platform.infrastructure.outbound.database.dao.ProjectContactDao;

import java.util.List;

@OutboundAdapter
public class ProjectContactInventoryDatabaseAdapter implements ProjectContactInventory {

    private final ProjectContactDao dao;

    public ProjectContactInventoryDatabaseAdapter(ProjectContactDao dao) {
        this.dao = dao;
    }

    @Override
    public void createAll(List<ProjectContact> projectContacts, Project project) {

        if (projectContacts == null || projectContacts.isEmpty()) {
            return;
        }

        dao.insertAll(projectContacts, project.id());
    }

    @Override
    public List<ProjectContact> findAllByProject(Project project) {
        return dao.findAllByProjectId(project.id());
    }

}
