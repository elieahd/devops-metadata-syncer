package devops.platform.domain.services;

import devops.platform.domain.inbound.GetProjects;
import devops.platform.domain.models.Project;
import devops.platform.domain.outbound.ProjectInventory;

import java.util.List;

@DomainService
public class ProjectService implements GetProjects {

    private final ProjectInventory projectInventory;

    public ProjectService(ProjectInventory projectInventory) {
        this.projectInventory = projectInventory;
    }

    @Override
    public List<Project> getAll() {
        return projectInventory.findAll();
    }
}
