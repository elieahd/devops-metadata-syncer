package devops.metadata.syncer.infrastructure.outbound.synchronizers.github;

import devops.metadata.syncer.infrastructure.outbound.OutboundAdapter;
import devops.metadata.syncer.infrastructure.outbound.github.GitHubClient;
import devops.metadata.syncer.infrastructure.outbound.synchronizers.github.mappers.GitHubPullRequestMapper;
import devops.metadata.syncer.infrastructure.outbound.synchronizers.github.mappers.GitHubReleaseMapper;
import devops.metadata.syncer.infrastructure.outbound.synchronizers.github.mappers.GitHubVulnerabilityMapper;
import devops.metadata.syncer.infrastructure.outbound.synchronizers.github.mappers.GitHubWorkflowMapper;
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
