package devops.metadata.syncer.infrastructure.outbound.database;

import devops.metadata.syncer.domain.models.Project;
import devops.metadata.syncer.domain.models.PullRequest;
import devops.metadata.syncer.domain.models.PullRequestReview;
import devops.metadata.syncer.domain.models.Repository;
import devops.metadata.syncer.domain.models.RepositorySource;
import devops.metadata.syncer.domain.models.assertions.PullRequestAssertions;
import devops.metadata.syncer.infrastructure.OutboundDatabaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static devops.metadata.syncer.domain.models.ModelRandomizer.aPullRequest;
import static devops.metadata.syncer.domain.models.ModelRandomizer.aPullRequestReview;
import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;

class PullRequestInventoryDatabaseAdapterTest extends OutboundDatabaseIntegrationTest {

    @Autowired
    private ProjectInventoryDatabaseAdapter projectInventory;

    @Autowired
    private RepositoryInventoryDatabaseAdapter repositoryInventory;

    @Autowired
    private PullRequestInventoryDatabaseAdapter sut;

    @Test
    void insertAll_shouldInsertAllPullRequestsForAGivenRepository() {
        // Arrange
        Project project = createProject();
        Repository repository = createRepository(project);
        Repository anotherRepository = createRepository(project);

        PullRequest pullRequest1 = aPullRequest(List.of(aPullRequestReview(), aPullRequestReview()));
        PullRequest pullRequest2 = aPullRequest(new ArrayList<>());
        List<PullRequestReview> nullReviews = null;
        PullRequest pullRequest3 = aPullRequest(nullReviews);
        PullRequest pullRequest4 = aPullRequest(List.of(aPullRequestReview(), aPullRequestReview()));

        sut.insertAll(repository.id(), List.of(pullRequest1, pullRequest2, pullRequest3));
        sut.insertAll(anotherRepository.id(), List.of(pullRequest4));
        // Act
        List<PullRequest> repositoryPullRequests = sut.findAllByRepositoryId(repository.id());
        // Assert
        assertThat(repositoryPullRequests)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);

        PullRequestAssertions.assertThat(repositoryPullRequests.getFirst()).isEqualTo(pullRequest1);
        PullRequestAssertions.assertThat(repositoryPullRequests.get(1)).isEqualTo(pullRequest2);
        PullRequestAssertions.assertThat(repositoryPullRequests.get(2)).isEqualTo(pullRequest3);
    }

    @Test
    void insertAll_shouldNotThrowException_whenPullRequestsIsEmpty() {
        // Arrange
        Project project = createProject();
        Repository repository = createRepository(project);
        List<PullRequest> pullRequests = new ArrayList<>();
        // Act
        sut.insertAll(repository.id(), pullRequests);
    }

    @Test
    void insertAll_shouldNotThrowException_whenPullRequestsIsNull() {
        // Arrange
        Project project = createProject();
        Repository repository = createRepository(project);
        List<PullRequest> pullRequests = null;
        // Act
        sut.insertAll(repository.id(), pullRequests);
    }

    @Test
    void deleteAllByRepositoryId_shouldDeleteAllPullRequestsForAGivenRepository() {
        // Arrange
        Project project = createProject();
        Repository repository = createRepository(project);
        Repository anotherRepository = createRepository(project);

        PullRequest pullRequest1 = aPullRequest(List.of(aPullRequestReview(), aPullRequestReview()));
        PullRequest pullRequest2 = aPullRequest(new ArrayList<>());
        List<PullRequestReview> nullReviews = null;
        PullRequest pullRequest3 = aPullRequest(nullReviews);
        PullRequest pullRequest4 = aPullRequest(List.of(aPullRequestReview(), aPullRequestReview()));

        sut.insertAll(repository.id(), List.of(pullRequest1, pullRequest2, pullRequest3));
        sut.insertAll(anotherRepository.id(), List.of(pullRequest4));

        // Act
        sut.deleteAllByRepositoryId(repository.id());
        // Assert
        List<PullRequest> repositoryPullRequests = sut.findAllByRepositoryId(repository.id());
        assertThat(repositoryPullRequests)
                .isNotNull()
                .isEmpty();
        List<PullRequest> anotherRepositoryPullRequests = sut.findAllByRepositoryId(anotherRepository.id());
        assertThat(anotherRepositoryPullRequests)
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
