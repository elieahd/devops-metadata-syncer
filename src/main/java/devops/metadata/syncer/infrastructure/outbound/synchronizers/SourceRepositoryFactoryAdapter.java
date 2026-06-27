package devops.metadata.syncer.infrastructure.outbound.synchronizers;

import devops.metadata.syncer.domain.exceptions.SourceNotFoundException;
import devops.metadata.syncer.domain.models.RepositorySource;
import devops.metadata.syncer.domain.outbound.SourceRepositoryFactory;
import devops.metadata.syncer.domain.outbound.SourceRepositoryInventory;
import devops.metadata.syncer.infrastructure.outbound.OutboundAdapter;
import devops.metadata.syncer.infrastructure.outbound.synchronizers.github.EnterpriseGitHubRepositoryInventory;
import devops.metadata.syncer.infrastructure.outbound.synchronizers.github.GithubRepositoryInventory;

@OutboundAdapter
public class SourceRepositoryFactoryAdapter implements SourceRepositoryFactory {

    private final GithubRepositoryInventory github;
    private final EnterpriseGitHubRepositoryInventory enterpriseGithub;

    public SourceRepositoryFactoryAdapter(GithubRepositoryInventory github,
                                          EnterpriseGitHubRepositoryInventory enterpriseGithub) {
        this.github = github;
        this.enterpriseGithub = enterpriseGithub;
    }

    @Override
    public SourceRepositoryInventory of(RepositorySource source) throws SourceNotFoundException {
        return switch (source) {
            case GITHUB -> github;
            case ENTERPRISE_GITHUB -> enterpriseGithub;
            case null -> throw new NullPointerException("Source shouldn't be null");
            default -> throw new SourceNotFoundException(source.name());
        };
    }

}
