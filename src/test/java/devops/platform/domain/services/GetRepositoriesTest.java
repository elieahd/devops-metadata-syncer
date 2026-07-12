package devops.platform.domain.services;

import devops.platform.domain.exceptions.ProjectNotFoundException;
import devops.platform.domain.inbound.GetRepositories;
import devops.platform.domain.models.Project;
import devops.platform.domain.models.Repository;
import devops.platform.domain.models.randomizers.ProjectRandomizer;
import devops.platform.domain.models.randomizers.RepositoryRandomizer;
import devops.platform.domain.outbound.ProjectInventory;
import devops.platform.domain.outbound.ProjectInventoryStub;
import devops.platform.domain.outbound.RepositoryInventoryStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class GetRepositoriesTest {

    private RepositoryInventoryStub repositoryInventory;
    private ProjectInventory projectInventory;
    private GetRepositories sut;

    @BeforeEach
    void setup() {
        repositoryInventory = new RepositoryInventoryStub();
        projectInventory = new ProjectInventoryStub();
        sut = new RepositoryService(repositoryInventory, projectInventory);
    }

    @Test
    void getAllByProjectKey_shouldReturnAllRepositoriesByProjectKey() throws ProjectNotFoundException {
        // Arrange
        Project project = ProjectRandomizer.random();
        projectInventory.create(project);
        Repository repository1 = RepositoryRandomizer.random();
        repositoryInventory.create(project, repository1);
        Repository repository2 = RepositoryRandomizer.random();
        repositoryInventory.create(project, repository2);

        Project anotherProject = ProjectRandomizer.random();
        projectInventory.create(anotherProject);
        Repository anotherRepository = RepositoryRandomizer.random();
        repositoryInventory.create(anotherProject, anotherRepository);
        // Act
        List<Repository> repositories = sut.getAllByProjectKey(project.key());
        // Assert
        assertThat(repositories)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .contains(repository1, repository2);
    }

    @Test
    void getAllByProjectKey_shouldThrowProjectNotFound_whenProjectNotExists() {
        // Arrange
        String projectKey = random(String.class);
        // Act
        Throwable thrown = catchThrowable(() -> sut.getAllByProjectKey(projectKey));
        // Assert
        assertThat(thrown)
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessage(String.format("Project %s not found", projectKey));
    }

}
