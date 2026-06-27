package devops.metadata.syncer.infrastructure.outbound.synchronizers.github;

import devops.metadata.syncer.domain.models.Pipeline;
import devops.metadata.syncer.domain.models.PullRequest;
import devops.metadata.syncer.domain.models.Release;
import devops.metadata.syncer.domain.models.Vulnerability;
import devops.metadata.syncer.domain.outbound.SourceRepositoryInventory;
import devops.metadata.syncer.infrastructure.outbound.github.GitHubClient;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubPullRequestReview;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubWorkflowRun;
import devops.metadata.syncer.infrastructure.outbound.synchronizers.github.mappers.GitHubPullRequestMapper;
import devops.metadata.syncer.infrastructure.outbound.synchronizers.github.mappers.GitHubReleaseMapper;
import devops.metadata.syncer.infrastructure.outbound.synchronizers.github.mappers.GitHubVulnerabilityMapper;
import devops.metadata.syncer.infrastructure.outbound.synchronizers.github.mappers.GitHubWorkflowMapper;

import java.util.Comparator;
import java.util.List;

public abstract class BaseGitHubRepositoryInventory implements SourceRepositoryInventory {

    private final GitHubClient gitHubClient;
    private final GitHubPullRequestMapper prMapper;
    private final GitHubWorkflowMapper workflowMapper;
    private final GitHubReleaseMapper releaseMapper;
    private final GitHubVulnerabilityMapper vulnerabilityMapper;

    protected BaseGitHubRepositoryInventory(GitHubClient gitHubClient,
                                            GitHubPullRequestMapper prMapper,
                                            GitHubWorkflowMapper workflowMapper,
                                            GitHubReleaseMapper releaseMapper,
                                            GitHubVulnerabilityMapper vulnerabilityMapper) {
        this.gitHubClient = gitHubClient;
        this.prMapper = prMapper;
        this.workflowMapper = workflowMapper;
        this.releaseMapper = releaseMapper;
        this.vulnerabilityMapper = vulnerabilityMapper;
    }

    @Override
    public List<PullRequest> findAllPullRequests(String organization,
                                                 String repository) {
        return gitHubClient.findAllPullRequests(organization, repository, "closed")
                .stream()
                .map(pr -> {
                    List<GitHubPullRequestReview> reviews = gitHubClient.findAllPullRequestReviews(organization, repository, pr.number());
                    return prMapper.map(pr, reviews);
                })
                .toList();
    }

    @Override
    public List<Pipeline> findAllWorkflows(String organization,
                                           String repository) {
        return gitHubClient.findAllWorkflows(organization, repository)
                .stream()
                .map(workflow -> {
                    List<GitHubWorkflowRun> workflowRuns = gitHubClient.findAllWorkflowRuns(organization, repository, workflow.id());
                    return workflowMapper.map(workflow, workflowRuns);
                })
                .toList();
    }

    @Override
    public List<Release> findAllReleases(String organization,
                                         String repository) {
        return gitHubClient.findAllReleases(organization, repository)
                .stream()
                .filter(release -> !release.draft() && !release.preRelease())
                .map(releaseMapper::map)
                .sorted(Comparator.comparing(Release::publishedAt, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    @Override
    public List<Vulnerability> findAllVulnerabilities(String organization,
                                                      String repository) {
        return gitHubClient.findAllDependabotAlerts(organization, repository)
                .stream()
                .map(vulnerabilityMapper::map)
                .toList();
    }
}
