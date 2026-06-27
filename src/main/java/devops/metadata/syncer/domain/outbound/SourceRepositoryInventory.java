package devops.metadata.syncer.domain.outbound;

import devops.metadata.syncer.domain.models.Pipeline;
import devops.metadata.syncer.domain.models.PullRequest;
import devops.metadata.syncer.domain.models.Release;
import devops.metadata.syncer.domain.models.Vulnerability;

import java.util.List;

public interface SourceRepositoryInventory {

    List<PullRequest> findAllPullRequests(String organization, String repository);

    List<Pipeline> findAllWorkflows(String organization, String repository);

    List<Release> findAllReleases(String organization, String repository);

    List<Vulnerability> findAllVulnerabilities(String organization, String repository);

}
