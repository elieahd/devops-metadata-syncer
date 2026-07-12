package devops.platform.domain.services;

import devops.platform.domain.inbound.GetProjects;
import devops.platform.domain.models.Project;
import devops.platform.domain.models.randomizers.ProjectRandomizer;
import devops.platform.domain.outbound.ProjectInventory;
import devops.platform.domain.outbound.ProjectInventoryStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GetProjectsTest {

    private ProjectInventory projectInventory;
    private GetProjects sut;

    @BeforeEach
    void setup() {
        projectInventory = new ProjectInventoryStub();
        sut = new ProjectService(projectInventory);
    }

    @Test
    void getAll_shouldReturnAllProjects() {
        // Arrange
        Project project1 = ProjectRandomizer.random();
        projectInventory.create(project1);
        Project project2 = ProjectRandomizer.random();
        projectInventory.create(project2);
        Project project3 = ProjectRandomizer.random();
        projectInventory.create(project3);
        // Act
        List<Project> projects = sut.getAll();
        // Assert
        assertThat(projects)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .contains(project1, project2, project3);
    }

}
