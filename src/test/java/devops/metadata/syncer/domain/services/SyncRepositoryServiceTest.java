package devops.metadata.syncer.domain.services;

import devops.metadata.syncer.domain.exceptions.SourceNotFoundException;
import devops.metadata.syncer.domain.inbound.SyncRepository;
import devops.metadata.syncer.domain.models.Pipeline;
import devops.metadata.syncer.domain.models.PullRequest;
import devops.metadata.syncer.domain.models.Release;
import devops.metadata.syncer.domain.models.Repository;
import devops.metadata.syncer.domain.models.RepositorySource;
import devops.metadata.syncer.domain.models.Vulnerability;
import devops.metadata.syncer.domain.models.assertions.PipelineAssertions;
import devops.metadata.syncer.domain.models.assertions.PullRequestAssertions;
import devops.metadata.syncer.domain.models.assertions.ReleaseAssertions;
import devops.metadata.syncer.domain.models.assertions.VulnerabilityAssertions;
import devops.metadata.syncer.domain.outbound.PipelineInventory;
import devops.metadata.syncer.domain.outbound.PipelineInventoryStub;
import devops.metadata.syncer.domain.outbound.PullRequestInventory;
import devops.metadata.syncer.domain.outbound.PullRequestInventoryStub;
import devops.metadata.syncer.domain.outbound.ReleaseInventory;
import devops.metadata.syncer.domain.outbound.ReleaseInventoryStub;
import devops.metadata.syncer.domain.outbound.RepositoryInventoryStub;
import devops.metadata.syncer.domain.outbound.SourceRepositoryFactory;
import devops.metadata.syncer.domain.outbound.SourceRepositoryInventoryStub;
import devops.metadata.syncer.domain.outbound.VulnerabilityInventory;
import devops.metadata.syncer.domain.outbound.VulnerabilityInventoryStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static devops.metadata.syncer.domain.models.ModelRandomizer.aPipeline;
import static devops.metadata.syncer.domain.models.ModelRandomizer.aPullRequest;
import static devops.metadata.syncer.domain.models.ModelRandomizer.aRelease;
import static devops.metadata.syncer.domain.models.ModelRandomizer.aRepository;
import static devops.metadata.syncer.domain.models.ModelRandomizer.aVulnerability;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

class SyncRepositoryServiceTest {

    private RepositoryInventoryStub repositoryInventory;
    private PullRequestInventory pullRequestInventory;
    private PipelineInventory pipelineInventory;
    private ReleaseInventory releaseInventory;
    private VulnerabilityInventory vulnerabilityInventory;
    private SourceRepositoryInventoryStub sourceRepositoryInventory;
    private SyncRepository sut;

    @BeforeEach
    void setup() {
        repositoryInventory = new RepositoryInventoryStub();
        sourceRepositoryInventory = new SourceRepositoryInventoryStub();
        SourceRepositoryFactory factory = source -> {
            if (RepositorySource.GITHUB.equals(source)) {
                return sourceRepositoryInventory;
            }
            throw new SourceNotFoundException(source.name());
        };
        pullRequestInventory = new PullRequestInventoryStub();
        pipelineInventory = new PipelineInventoryStub();
        releaseInventory = new ReleaseInventoryStub();
        vulnerabilityInventory = new VulnerabilityInventoryStub();
        sut = new SyncRepositoryService(
                repositoryInventory,
                factory,
                pullRequestInventory,
                pipelineInventory,
                releaseInventory,
                vulnerabilityInventory
        );
    }

    @Test
    void sync_shouldSyncPullRequests() throws SourceNotFoundException {
        // Arrange
        Repository repository = aRepository(RepositorySource.GITHUB);
        List<PullRequest> existingPRs = List.of(
                aPullRequest(),
                aPullRequest()
        );
        pullRequestInventory.insertAll(repository.id(), existingPRs);
        List<PullRequest> newPRs = List.of(
                aPullRequest(),
                aPullRequest()
        );
        sourceRepositoryInventory.pushPullRequests(repository.organization(), repository.name(), newPRs);
        // Act
        sut.sync(repository);
        // Assert
        List<PullRequest> syncedPRs = pullRequestInventory.findAllByRepositoryId(repository.id());
        assertThat(syncedPRs)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
        PullRequestAssertions.assertThat(syncedPRs.getFirst()).isEqualTo(newPRs.getFirst());
        PullRequestAssertions.assertThat(syncedPRs.getLast()).isEqualTo(newPRs.getLast());
    }

    @Test
    void sync_shouldSyncPipelines() throws SourceNotFoundException {
        // Arrange
        Repository repository = aRepository(RepositorySource.GITHUB);
        List<Pipeline> existingPipelines = List.of(
                aPipeline(),
                aPipeline()
        );
        pipelineInventory.insertAll(repository.id(), existingPipelines);
        List<Pipeline> newPipelines = List.of(
                aPipeline(),
                aPipeline()
        );
        sourceRepositoryInventory.pushWorkflows(repository.organization(), repository.name(), newPipelines);
        // Act
        sut.sync(repository);
        // Assert
        List<Pipeline> syncedPipelines = pipelineInventory.findAllByRepositoryId(repository.id());
        assertThat(syncedPipelines)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
        PipelineAssertions.assertThat(syncedPipelines.getFirst()).isEqualTo(newPipelines.getFirst());
        PipelineAssertions.assertThat(syncedPipelines.getLast()).isEqualTo(newPipelines.getLast());
    }

    @Test
    void sync_shouldSyncReleases() throws SourceNotFoundException {
        // Arrange
        Repository repository = aRepository(RepositorySource.GITHUB);
        List<Release> existingReleases = List.of(
                aRelease(),
                aRelease()
        );
        releaseInventory.insertAll(repository.id(), existingReleases);
        List<Release> newReleases = List.of(
                aRelease(),
                aRelease()
        );
        sourceRepositoryInventory.pushReleases(repository.organization(), repository.name(), newReleases);
        // Act
        sut.sync(repository);
        // Assert
        List<Release> syncedReleases = releaseInventory.findAllByRepositoryId(repository.id());
        assertThat(syncedReleases)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
        ReleaseAssertions.assertThat(syncedReleases.getFirst()).isEqualTo(newReleases.getFirst());
        ReleaseAssertions.assertThat(syncedReleases.getLast()).isEqualTo(newReleases.getLast());
    }

    @Test
    void sync_shouldSyncVulnerabilities() throws SourceNotFoundException {
        // Arrange
        Repository repository = aRepository(RepositorySource.GITHUB);
        List<Vulnerability> existingVulnerabilities = List.of(
                aVulnerability(),
                aVulnerability()
        );
        vulnerabilityInventory.insertAll(repository.id(), existingVulnerabilities);
        List<Vulnerability> newVulnerabilities = List.of(
                aVulnerability(),
                aVulnerability()
        );
        sourceRepositoryInventory.pushVulnerabilities(repository.organization(), repository.name(), newVulnerabilities);
        // Act
        sut.sync(repository);
        // Assert
        List<Vulnerability> syncedVulnerabilities = vulnerabilityInventory.findAllByRepositoryId(repository.id());
        assertThat(syncedVulnerabilities)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
        VulnerabilityAssertions.assertThat(syncedVulnerabilities.getFirst()).isEqualTo(newVulnerabilities.getFirst());
        VulnerabilityAssertions.assertThat(syncedVulnerabilities.getLast()).isEqualTo(newVulnerabilities.getLast());
    }

    @Test
    void sync_shouldUpdateLastSyncTime() throws SourceNotFoundException {
        // Arrange
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        Repository repository = aRepository(RepositorySource.GITHUB, yesterday);
        // Act
        sut.sync(repository);
        // Assert
        LocalDateTime lastSyncTime = repositoryInventory.getLastSyncTime(repository);
        assertThat(lastSyncTime)
                .isNotNull()
                .isCloseTo(LocalDateTime.now(), within(Duration.ofSeconds(5)));
    }

    @Test
    void sync_shouldThrowException_whenSourceNotFound() {
        // Arrange
        Repository repository = aRepository(RepositorySource.UNKNOWN);
        // Act
        Throwable thrown = catchThrowable(() -> sut.sync(repository));
        // Assert
        assertThat(thrown).isInstanceOf(SourceNotFoundException.class);
    }

}
