package devops.platform.infrastructure.outbound.synchronizers.github;

import devops.platform.domain.outbound.SourceRepositoryInventory;
import devops.platform.infrastructure.outbound.OutboundAdapter;
import devops.platform.infrastructure.outbound.github.GitHubClient;
import devops.platform.infrastructure.outbound.synchronizers.github.mappers.GitHubPullRequestMapper;
import devops.platform.infrastructure.outbound.synchronizers.github.mappers.GitHubReleaseMapper;
import devops.platform.infrastructure.outbound.synchronizers.github.mappers.GitHubVulnerabilityMapper;
import devops.platform.infrastructure.outbound.synchronizers.github.mappers.GitHubWorkflowMapper;
import org.springframework.beans.factory.annotation.Qualifier;

@OutboundAdapter
public class GithubRepositoryInventory extends BaseGitHubRepositoryInventory implements SourceRepositoryInventory {

    public GithubRepositoryInventory(@Qualifier("githubClient") GitHubClient gitHubClient,
                                     GitHubPullRequestMapper prMapper,
                                     GitHubWorkflowMapper workflowMapper,
                                     GitHubReleaseMapper releaseMapper,
                                     GitHubVulnerabilityMapper vulnerabilityMapper) {
        super(
                gitHubClient,
                prMapper,
                workflowMapper,
                releaseMapper,
                vulnerabilityMapper
        );
    }

}
