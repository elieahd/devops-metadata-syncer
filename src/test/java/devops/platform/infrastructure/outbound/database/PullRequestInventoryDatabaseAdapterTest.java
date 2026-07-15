package devops.platform.infrastructure.outbound.database;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.PullRequest;
import devops.platform.domain.models.Repository;
import devops.platform.domain.models.assertions.PullRequestAssertions;
import devops.platform.domain.models.randomizers.PullRequestRandomizer;
import devops.platform.infrastructure.OutboundDatabaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PullRequestInventoryDatabaseAdapterTest extends OutboundDatabaseIntegrationTest {

    @Autowired
    private PullRequestInventoryDatabaseAdapter sut;

    @Test
    void insertAll_shouldInsertAllPullRequestsForAGivenRepository() {
        // Arrange
        Project project = createProject();
        Repository repository = createRepository(project);
        Repository anotherRepository = createRepository(project);

        PullRequest pullRequest1 = PullRequestRandomizer.random();
        PullRequest pullRequest2 = PullRequestRandomizer.builder().reviews(List.of()).build();
        PullRequest pullRequest3 = PullRequestRandomizer.builder().reviews(null).build();
        PullRequest pullRequest4 = PullRequestRandomizer.random();

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

        PullRequest pullRequest1 = PullRequestRandomizer.random();
        PullRequest pullRequest2 = PullRequestRandomizer.builder().reviews(List.of()).build();
        PullRequest pullRequest3 = PullRequestRandomizer.builder().reviews(null).build();
        PullRequest pullRequest4 = PullRequestRandomizer.random();

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

}
