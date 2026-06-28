package devops.metadata.syncer.infrastructure.outbound.database;

import devops.metadata.syncer.domain.models.Pipeline;
import devops.metadata.syncer.domain.models.Project;
import devops.metadata.syncer.domain.models.Repository;
import devops.metadata.syncer.domain.models.RepositorySource;
import devops.metadata.syncer.domain.models.assertions.PipelineAssertions;
import devops.metadata.syncer.domain.models.randomizers.PipelineRandomizer;
import devops.metadata.syncer.infrastructure.OutboundDatabaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;

class PipelineInventoryDatabaseAdapterTest extends OutboundDatabaseIntegrationTest {

    @Autowired
    private ProjectInventoryDatabaseAdapter projectInventory;

    @Autowired
    private RepositoryInventoryDatabaseAdapter repositoryInventory;

    @Autowired
    private PipelineInventoryDatabaseAdapter sut;

    @Test
    void insertAll_shouldInsertAllPipelinesForAGivenRepository() {
        // Arrange
        Project project = createProject();
        Repository repository = createRepository(project);
        Repository anotherRepository = createRepository(project);

        Pipeline pipeline1 = PipelineRandomizer.random();
        Pipeline pipeline2 = PipelineRandomizer.builder()
                .runs(List.of())
                .build();
        Pipeline pipeline3 = PipelineRandomizer.builder()
                .runs(null)
                .build();
        Pipeline pipeline4 = PipelineRandomizer.random();

        sut.insertAll(repository.id(), List.of(pipeline1, pipeline2, pipeline3));
        sut.insertAll(anotherRepository.id(), List.of(pipeline4));
        // Act
        List<Pipeline> repositoryPipelines = sut.findAllByRepositoryId(repository.id());
        // Assert
        assertThat(repositoryPipelines)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);

        PipelineAssertions.assertThat(repositoryPipelines.getFirst()).isEqualTo(pipeline1);
        PipelineAssertions.assertThat(repositoryPipelines.get(1)).isEqualTo(pipeline2);
        PipelineAssertions.assertThat(repositoryPipelines.get(2)).isEqualTo(pipeline3);
    }

    @Test
    void insertAll_shouldNotThrowException_whenPipelinesIsEmpty() {
        // Arrange
        Project project = createProject();
        Repository repository = createRepository(project);
        List<Pipeline> pipelines = new ArrayList<>();
        // Act
        sut.insertAll(repository.id(), pipelines);
    }

    @Test
    void insertAll_shouldNotThrowException_whenPipelinesIsNull() {
        // Arrange
        Project project = createProject();
        Repository repository = createRepository(project);
        List<Pipeline> pipelines = null;
        // Act
        sut.insertAll(repository.id(), pipelines);
    }

    @Test
    void deleteAllByRepositoryId_shouldDeleteAllPipelinesForAGivenRepository() {
        // Arrange
        Project project = createProject();
        Repository repository = createRepository(project);
        Repository anotherRepository = createRepository(project);

        Pipeline pipeline1 = PipelineRandomizer.random();
        Pipeline pipeline2 = PipelineRandomizer.builder().runs(List.of()).build();
        Pipeline pipeline3 = PipelineRandomizer.builder().runs(null).build();
        Pipeline pipeline4 = PipelineRandomizer.random();

        sut.insertAll(repository.id(), List.of(pipeline1, pipeline2, pipeline3));
        sut.insertAll(anotherRepository.id(), List.of(pipeline4));

        // Act
        sut.deleteAllByRepositoryId(repository.id());
        // Assert
        List<Pipeline> repositoryPipelines = sut.findAllByRepositoryId(repository.id());
        assertThat(repositoryPipelines)
                .isNotNull()
                .isEmpty();
        List<Pipeline> anotherRepositoryPipelines = sut.findAllByRepositoryId(anotherRepository.id());
        assertThat(anotherRepositoryPipelines)
                .isNotNull()
                .isNotEmpty();
    }

    private Project createProject() {
        Project project = Project.of(random(String.class), random(String.class));
        return projectInventory.create(project);
    }

    private Repository createRepository(Project project) {
        Repository repository = Repository.of(random(String.class), random(String.class), random(RepositorySource.class));
        return repositoryInventory.create(project.id(), repository);
    }

}
