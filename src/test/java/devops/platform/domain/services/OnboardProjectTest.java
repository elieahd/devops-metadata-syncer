package devops.platform.domain.services;

import devops.platform.domain.exceptions.OrganizationNotFoundException;
import devops.platform.domain.exceptions.ProjectAlreadyExistsException;
import devops.platform.domain.inbound.OnboardProject;
import devops.platform.domain.models.Organization;
import devops.platform.domain.models.Project;
import devops.platform.domain.models.ProjectContact;
import devops.platform.domain.models.randomizers.OrganizationRandomizer;
import devops.platform.domain.models.randomizers.ProjectContactRandomizer;
import devops.platform.domain.models.randomizers.ProjectRandomizer;
import devops.platform.domain.outbound.OrganizationInventory;
import devops.platform.domain.outbound.OrganizationInventoryStub;
import devops.platform.domain.outbound.ProjectContactInventory;
import devops.platform.domain.outbound.ProjectContactInventoryStub;
import devops.platform.domain.outbound.ProjectInventory;
import devops.platform.domain.outbound.ProjectInventoryStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class OnboardProjectTest {

    private ProjectInventory projectInventory;
    private OrganizationInventory organizationInventory;
    private ProjectContactInventory projectContactInventory;
    private OnboardProject sut;

    @BeforeEach
    void setup() {
        projectInventory = new ProjectInventoryStub();
        organizationInventory = new OrganizationInventoryStub();
        projectContactInventory = new ProjectContactInventoryStub();
        sut = new ProjectService(projectInventory, organizationInventory, projectContactInventory);
    }

    @Test
    void onboard_shouldThrowOrganizationNotFound_whenOrganizationNotFoundByAcronym() {
        // Arrange
        String organizationAcronym = random(String.class);
        String projectKey = random(String.class);
        String projectName = random(String.class);
        List<ProjectContact> contacts = List.of(
                ProjectContactRandomizer.random(),
                ProjectContactRandomizer.random()
        );
        // Act
        Throwable thrown = catchThrowable(() -> sut.onboard(organizationAcronym, projectKey, projectName, contacts));
        // Assert
        assertThat(thrown)
                .isInstanceOf(OrganizationNotFoundException.class)
                .hasMessage("Organization '%s' not found".formatted(organizationAcronym));
    }

    @Test
    void onboard_shouldThrowProjectAlreadyExists() {
        // Arrange
        Organization organization = organizationInventory.create(OrganizationRandomizer.random());
        Project project = projectInventory.create(ProjectRandomizer.random(), organization);
        String organizationAcronym = organization.acronym();
        String projectKey = project.key();
        String projectName = random(String.class);
        List<ProjectContact> contacts = List.of(
                ProjectContactRandomizer.random(),
                ProjectContactRandomizer.random()
        );
        // Act
        Throwable thrown = catchThrowable(() -> sut.onboard(organizationAcronym, projectKey, projectName, contacts));
        // Assert
        assertThat(thrown)
                .isInstanceOf(ProjectAlreadyExistsException.class)
                .hasMessage("Project '%s' already exists".formatted(projectKey));
    }

    @Test
    void onboard_shouldCreateProjectWithContacts() throws OrganizationNotFoundException, ProjectAlreadyExistsException {
        // Arrange
        Organization organization = organizationInventory.create(OrganizationRandomizer.random());
        String organizationAcronym = organization.acronym();
        String projectKey = ProjectRandomizer.key();
        String projectName = random(String.class);
        List<ProjectContact> contacts = List.of(
                ProjectContactRandomizer.random(),
                ProjectContactRandomizer.random()
        );
        // Act
        Project project = sut.onboard(organizationAcronym, projectKey, projectName, contacts);
        // Assert
        assertThat(projectInventory.findAll())
                .hasSize(1)
                .contains(project);
        List<ProjectContact> projectContacts = projectContactInventory.findAllByProject(project);
        assertThat(projectContacts)
                .hasSize(2)
                .containsAll(contacts);
    }
}
