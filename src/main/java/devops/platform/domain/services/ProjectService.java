package devops.platform.domain.services;

import devops.platform.domain.exceptions.OrganizationNotFoundException;
import devops.platform.domain.exceptions.ProjectAlreadyExistsException;
import devops.platform.domain.inbound.GetProjects;
import devops.platform.domain.inbound.OnboardProject;
import devops.platform.domain.models.Organization;
import devops.platform.domain.models.Project;
import devops.platform.domain.models.ProjectContact;
import devops.platform.domain.outbound.OrganizationInventory;
import devops.platform.domain.outbound.ProjectContactInventory;
import devops.platform.domain.outbound.ProjectInventory;

import java.util.List;

@DomainService
public class ProjectService implements GetProjects, OnboardProject {

    private final ProjectInventory projectInventory;
    private final OrganizationInventory organizationInventory;
    private final ProjectContactInventory projectContactInventory;

    public ProjectService(ProjectInventory projectInventory,
                          OrganizationInventory organizationInventory,
                          ProjectContactInventory projectContactInventory) {
        this.projectInventory = projectInventory;
        this.organizationInventory = organizationInventory;
        this.projectContactInventory = projectContactInventory;
    }

    @Override
    public List<Project> getAll() {
        return projectInventory.findAll();
    }

    @Override
    public Project onboard(String organizationAcronym,
                           String projectKey,
                           String projectName,
                           List<ProjectContact> contacts) throws OrganizationNotFoundException, ProjectAlreadyExistsException {

        Organization organization = organizationInventory.findByAcronym(organizationAcronym)
                .orElseThrow(() -> new OrganizationNotFoundException(organizationAcronym));

        if (projectInventory.existsByKey(projectKey)) {
            throw new ProjectAlreadyExistsException(projectKey);
        }

        Project project = projectInventory.create(
                Project.of(projectKey, projectName),
                organization
        );

        projectContactInventory.createAll(contacts, project);

        return project;
    }
}
