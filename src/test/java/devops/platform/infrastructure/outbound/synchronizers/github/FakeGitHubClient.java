package devops.platform.infrastructure.outbound.synchronizers.github;

import devops.platform.infrastructure.outbound.github.GitHubClient;
import devops.platform.infrastructure.outbound.github.models.GitHubDependabotAlert;
import devops.platform.infrastructure.outbound.github.models.GitHubPullRequest;
import devops.platform.infrastructure.outbound.github.models.GitHubPullRequestReview;
import devops.platform.infrastructure.outbound.github.models.GitHubRelease;
import devops.platform.infrastructure.outbound.github.models.GitHubWorkflow;
import devops.platform.infrastructure.outbound.github.models.GitHubWorkflowRun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeGitHubClient implements GitHubClient {

    Map<String, List<GitHubPullRequest>> pullRequestsByState = new HashMap<>();
    Map<Long, List<GitHubPullRequestReview>> reviewsByPrNumber = new HashMap<>();
    List<GitHubWorkflow> workflows = List.of();
    Map<Long, List<GitHubWorkflowRun>> runsByWorkflowId = new HashMap<>();
    List<GitHubRelease> releases = List.of();
    List<GitHubDependabotAlert> dependabotAlerts = List.of();

    String requestedOrganization;
    String requestedRepository;
    String requestedState;

    @Override
    public List<GitHubPullRequest> findAllPullRequests(String organization, String repository, String state) {
        requestedOrganization = organization;
        requestedRepository = repository;
        requestedState = state;
        return pullRequestsByState.getOrDefault(state, List.of());
    }

    @Override
    public List<GitHubPullRequestReview> findAllPullRequestReviews(String organization, String repository, long pullRequestNumber) {
        return reviewsByPrNumber.getOrDefault(pullRequestNumber, List.of());
    }

    @Override
    public List<GitHubWorkflow> findAllWorkflows(String organization, String repository) {
        requestedOrganization = organization;
        requestedRepository = repository;
        return workflows;
    }

    @Override
    public List<GitHubWorkflowRun> findAllWorkflowRuns(String organization, String repository, long workflowId) {
        return runsByWorkflowId.getOrDefault(workflowId, List.of());
    }

    @Override
    public List<GitHubRelease> findAllReleases(String organization, String repository) {
        return releases;
    }

    @Override
    public List<GitHubDependabotAlert> findAllDependabotAlerts(String organization, String repository) {
        return dependabotAlerts;
    }
}