package devops.platform.domain.services;

import devops.platform.domain.exceptions.ProjectNotFoundException;
import devops.platform.domain.exceptions.SourceNotFoundException;
import devops.platform.domain.inbound.SyncProject;
import devops.platform.domain.models.Project;
import devops.platform.domain.models.Repository;
import devops.platform.domain.models.randomizers.ProjectRandomizer;
import devops.platform.domain.models.randomizers.RepositoryRandomizer;
import devops.platform.domain.outbound.ProjectInventory;
import devops.platform.domain.outbound.ProjectInventoryStub;
import devops.platform.domain.outbound.RepositoryInventory;
import devops.platform.domain.outbound.RepositoryInventoryStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class SyncProjectTest {

    private ProjectInventory projectInventory;
    private RepositoryInventory repositoryInventory;
    private SyncRepositoryStub syncRepository;
    private SyncProject sut;

    @BeforeEach
    void setup() {
        projectInventory = new ProjectInventoryStub();
        repositoryInventory = new RepositoryInventoryStub();
        syncRepository = new SyncRepositoryStub();
        sut = new SyncProjectService(
                projectInventory,
                repositoryInventory,
                syncRepository
        );
    }

    @Test
    void sync_shouldThrowProjectNotFound_whenProjectNotFound() {
        // Arrange
        String projectKey = random(String.class);
        // Act
        Throwable thrown = catchThrowable(() -> sut.sync(projectKey));
        // Assert
        assertThat(thrown)
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessage(String.format("Project %s not found", projectKey));
    }

    @Test
    void sync_shouldDoNothing_whenNoRepositoriesFoundPerProject() throws SourceNotFoundException, ProjectNotFoundException {
        // Arrange
        Project project = ProjectRandomizer.random();
        projectInventory.create(project);
        // Act
        sut.sync(project.key());
        // Assert
        assertThat(syncRepository.anySynced()).isFalse();
    }

    @Test
    void sync_shouldSyncAllRepositories() throws SourceNotFoundException, ProjectNotFoundException {
        // Arrange
        Project project = ProjectRandomizer.random();
        projectInventory.create(project);
        Repository repository1 = RepositoryRandomizer.random();
        repositoryInventory.create(project.id(), repository1);
        Repository repository2 = RepositoryRandomizer.random();
        repositoryInventory.create(project.id(), repository2);

        Project anotherProject = ProjectRandomizer.random();
        projectInventory.create(anotherProject);
        Repository repository3 = RepositoryRandomizer.random();
        repositoryInventory.create(anotherProject.id(), repository3);
        // Act
        sut.sync(project.key());
        // Assert
        assertThat(syncRepository.hasSynced(repository1.id())).isTrue();
        assertThat(syncRepository.hasSynced(repository2.id())).isTrue();
        assertThat(syncRepository.hasSynced(repository3.id())).isFalse();
    }
}
