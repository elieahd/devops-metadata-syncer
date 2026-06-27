package devops.metadata.syncer.domain.services;

import devops.metadata.syncer.domain.exceptions.SourceNotFoundException;
import devops.metadata.syncer.domain.inbound.SyncRepository;
import devops.metadata.syncer.domain.models.Pipeline;
import devops.metadata.syncer.domain.models.PullRequest;
import devops.metadata.syncer.domain.models.Release;
import devops.metadata.syncer.domain.models.Repository;
import devops.metadata.syncer.domain.models.Vulnerability;
import devops.metadata.syncer.domain.outbound.PipelineInventory;
import devops.metadata.syncer.domain.outbound.PullRequestInventory;
import devops.metadata.syncer.domain.outbound.ReleaseInventory;
import devops.metadata.syncer.domain.outbound.RepositoryInventory;
import devops.metadata.syncer.domain.outbound.SourceRepositoryFactory;
import devops.metadata.syncer.domain.outbound.SourceRepositoryInventory;
import devops.metadata.syncer.domain.outbound.VulnerabilityInventory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@DomainService
public class SyncRepositoryService implements SyncRepository {

    private final RepositoryInventory repositoryInventory;
    private final SourceRepositoryFactory factory;
    private final PullRequestInventory pullRequestInventory;
    private final PipelineInventory pipelineInventory;
    private final ReleaseInventory releaseInventory;
    private final VulnerabilityInventory vulnerabilityInventory;

    public SyncRepositoryService(RepositoryInventory repositoryInventory,
                                 SourceRepositoryFactory factory,
                                 PullRequestInventory pullRequestInventory,
                                 PipelineInventory pipelineInventory,
                                 ReleaseInventory releaseInventory,
                                 VulnerabilityInventory vulnerabilityInventory) {
        this.repositoryInventory = repositoryInventory;
        this.factory = factory;
        this.pullRequestInventory = pullRequestInventory;
        this.pipelineInventory = pipelineInventory;
        this.releaseInventory = releaseInventory;
        this.vulnerabilityInventory = vulnerabilityInventory;
    }

    @Override
    public void sync(Repository repository) throws SourceNotFoundException {
        LocalDateTime syncTime = LocalDateTime.now(ZoneId.systemDefault());

        SourceRepositoryInventory sourceRepository = factory.of(repository.source());

        List<PullRequest> pullRequests = sourceRepository.findAllPullRequests(repository.organization(), repository.name());
        pullRequestInventory.deleteAllByRepositoryId(repository.id());
        pullRequestInventory.insertAll(repository.id(), pullRequests);

        List<Pipeline> pipelines = sourceRepository.findAllWorkflows(repository.organization(), repository.name());
        pipelineInventory.deleteAllByRepositoryId(repository.id());
        pipelineInventory.insertAll(repository.id(), pipelines);

        List<Release> releases = sourceRepository.findAllReleases(repository.organization(), repository.name());
        releaseInventory.deleteAllByRepositoryId(repository.id());
        releaseInventory.insertAll(repository.id(), releases);

        List<Vulnerability> vulnerabilities = sourceRepository.findAllVulnerabilities(repository.organization(), repository.name());
        vulnerabilityInventory.deleteAllByRepositoryId(repository.id());
        vulnerabilityInventory.insertAll(repository.id(), vulnerabilities);

        repositoryInventory.updateLastSyncTime(repository.id(), syncTime);
    }

}
