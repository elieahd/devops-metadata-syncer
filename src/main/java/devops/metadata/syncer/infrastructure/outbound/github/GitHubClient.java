package devops.metadata.syncer.infrastructure.outbound.github;

import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubDependabotAlert;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubPullRequest;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubPullRequestReview;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubRelease;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubWorkflow;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubWorkflowRun;

import java.util.List;

public interface GitHubClient {

    List<GitHubPullRequest> findAllPullRequests(String organization,
                                                String repository,
                                                String state);

    List<GitHubPullRequestReview> findAllPullRequestReviews(String organization,
                                                            String repository,
                                                            long pullRequestNumber);

    List<GitHubWorkflow> findAllWorkflows(String organization,
                                          String repository);

    List<GitHubWorkflowRun> findAllWorkflowRuns(String organization,
                                                String repository,
                                                long workflowId);

    List<GitHubRelease> findAllReleases(String organization,
                                        String repository);

    List<GitHubDependabotAlert> findAllDependabotAlerts(String organization,
                                                        String repository);
}
