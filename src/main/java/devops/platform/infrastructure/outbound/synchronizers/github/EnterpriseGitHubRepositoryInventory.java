package devops.platform.infrastructure.outbound.synchronizers.github;

import devops.platform.infrastructure.outbound.OutboundAdapter;
import devops.platform.infrastructure.outbound.github.GitHubClient;
import devops.platform.infrastructure.outbound.synchronizers.github.mappers.GitHubPullRequestMapper;
import devops.platform.infrastructure.outbound.synchronizers.github.mappers.GitHubReleaseMapper;
import devops.platform.infrastructure.outbound.synchronizers.github.mappers.GitHubVulnerabilityMapper;
import devops.platform.infrastructure.outbound.synchronizers.github.mappers.GitHubWorkflowMapper;
import org.springframework.beans.factory.annotation.Qualifier;

@OutboundAdapter
public class EnterpriseGitHubRepositoryInventory extends BaseGitHubRepositoryInventory {

    public EnterpriseGitHubRepositoryInventory(@Qualifier("enterpriseGithubClient") GitHubClient gitHubClient,
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
