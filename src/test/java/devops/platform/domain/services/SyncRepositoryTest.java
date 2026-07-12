package devops.platform.domain.services;

import devops.platform.domain.exceptions.RepositoryNotFoundException;
import devops.platform.domain.exceptions.SourceNotFoundException;
import devops.platform.domain.inbound.SyncRepository;
import devops.platform.domain.models.Pipeline;
import devops.platform.domain.models.PullRequest;
import devops.platform.domain.models.Release;
import devops.platform.domain.models.Repository;
import devops.platform.domain.models.RepositorySource;
import devops.platform.domain.models.Vulnerability;
import devops.platform.domain.models.assertions.PipelineAssertions;
import devops.platform.domain.models.assertions.PullRequestAssertions;
import devops.platform.domain.models.assertions.ReleaseAssertions;
import devops.platform.domain.models.assertions.VulnerabilityAssertions;
import devops.platform.domain.models.randomizers.PipelineRandomizer;
import devops.platform.domain.models.randomizers.PullRequestRandomizer;
import devops.platform.domain.models.randomizers.ReleaseRandomizer;
import devops.platform.domain.models.randomizers.RepositoryRandomizer;
import devops.platform.domain.models.randomizers.VulnerabilityRandomizer;
import devops.platform.domain.outbound.PipelineInventory;
import devops.platform.domain.outbound.PipelineInventoryStub;
import devops.platform.domain.outbound.PullRequestInventory;
import devops.platform.domain.outbound.PullRequestInventoryStub;
import devops.platform.domain.outbound.ReleaseInventory;
import devops.platform.domain.outbound.ReleaseInventoryStub;
import devops.platform.domain.outbound.RepositoryInventoryStub;
import devops.platform.domain.outbound.SourceRepositoryFactory;
import devops.platform.domain.outbound.SourceRepositoryInventoryStub;
import devops.platform.domain.outbound.VulnerabilityInventory;
import devops.platform.domain.outbound.VulnerabilityInventoryStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

class SyncRepositoryTest {

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
    void sync_shouldThrowException_whenRepositoryNotFound() {
        // Arrange
        String organization = random(String.class);
        String repositoryName = random(String.class);
        RepositorySource source = random(RepositorySource.class);
        // Act
        Throwable thrown = catchThrowable(() -> sut.sync(organization, repositoryName, source));
        // Assert
        assertThat(thrown)
                .isInstanceOf(RepositoryNotFoundException.class)
                .hasMessage(String.format("Repository %s/%s (%s) not found", organization, repositoryName, source));
    }

    @Test
    void syncRaw_shouldUpdateLastSyncTime() throws SourceNotFoundException {
        // Arrange
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        Repository repository = repositoryInventory.create(random(Long.class), RepositoryRandomizer.builder()
                .source(RepositorySource.GITHUB)
                .lastSyncTime(yesterday)
                .build());
        String organization = repository.organization();
        String repositoryName = repository.name();
        RepositorySource source = repository.source();
        // Act
        sut.sync(organization, repositoryName, source);
        // Assert
        LocalDateTime lastSyncTime = repositoryInventory.getLastSyncTime(repository);
        assertThat(lastSyncTime)
                .isNotNull()
                .isCloseTo(LocalDateTime.now(), within(Duration.ofSeconds(5)));
    }

    @Test
    void sync_shouldSyncPullRequests() throws SourceNotFoundException {
        // Arrange
        Repository repository = RepositoryRandomizer.builder()
                .source(RepositorySource.GITHUB)
                .build();
        List<PullRequest> existingPRs = List.of(
                PullRequestRandomizer.random(),
                PullRequestRandomizer.random()
        );
        pullRequestInventory.insertAll(repository.id(), existingPRs);
        List<PullRequest> newPRs = List.of(
                PullRequestRandomizer.random(),
                PullRequestRandomizer.random()
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
        Repository repository = RepositoryRandomizer.builder()
                .source(RepositorySource.GITHUB)
                .build();
        List<Pipeline> existingPipelines = List.of(
                PipelineRandomizer.random(),
                PipelineRandomizer.random()
        );
        pipelineInventory.insertAll(repository.id(), existingPipelines);
        List<Pipeline> newPipelines = List.of(
                PipelineRandomizer.random(),
                PipelineRandomizer.random()
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
        Repository repository = RepositoryRandomizer.builder()
                .source(RepositorySource.GITHUB)
                .build();
        List<Release> existingReleases = List.of(
                ReleaseRandomizer.random(),
                ReleaseRandomizer.random()
        );
        releaseInventory.insertAll(repository.id(), existingReleases);
        List<Release> newReleases = List.of(
                ReleaseRandomizer.random(),
                ReleaseRandomizer.random()
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
        Repository repository = RepositoryRandomizer.builder()
                .source(RepositorySource.GITHUB)
                .build();
        List<Vulnerability> existingVulnerabilities = List.of(
                VulnerabilityRandomizer.random(),
                VulnerabilityRandomizer.random()
        );
        vulnerabilityInventory.insertAll(repository.id(), existingVulnerabilities);
        List<Vulnerability> newVulnerabilities = List.of(
                VulnerabilityRandomizer.random(),
                VulnerabilityRandomizer.random()
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
        Repository repository = RepositoryRandomizer.builder()
                .source(RepositorySource.GITHUB)
                .lastSyncTime(yesterday)
                .build();
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
        Repository repository = RepositoryRandomizer.builder()
                .source(RepositorySource.UNKNOWN)
                .build();
        // Act
        Throwable thrown = catchThrowable(() -> sut.sync(repository));
        // Assert
        assertThat(thrown).isInstanceOf(SourceNotFoundException.class);
    }

}
