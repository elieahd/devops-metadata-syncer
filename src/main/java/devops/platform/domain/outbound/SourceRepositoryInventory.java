package devops.platform.domain.outbound;

import devops.platform.domain.models.Pipeline;
import devops.platform.domain.models.PullRequest;
import devops.platform.domain.models.Release;
import devops.platform.domain.models.Vulnerability;

import java.util.List;

public interface SourceRepositoryInventory {

    List<PullRequest> findAllPullRequests(String organization,
                                          String repository);

    List<Pipeline> findAllWorkflows(String organization,
                                    String repository);

    List<Release> findAllReleases(String organization,
                                  String repository);

    List<Vulnerability> findAllVulnerabilities(String organization,
                                               String repository);

}
