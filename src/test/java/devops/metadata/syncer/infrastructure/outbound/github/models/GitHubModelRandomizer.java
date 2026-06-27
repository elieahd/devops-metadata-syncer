package devops.metadata.syncer.infrastructure.outbound.github.models;

import java.time.OffsetDateTime;

import static com.devt.randomizer.RandomizerUtils.random;

public class GitHubModelRandomizer {

    private GitHubModelRandomizer() {
    }

    public static GitHubRelease aGitHubRelease() {
        return new GitHubRelease(
                random(String.class),
                random(String.class),
                random(Boolean.class),
                random(Boolean.class),
                random(OffsetDateTime.class)
        );
    }

    public static GitHubPullRequest aGitHubPullRequest(GitHubUser author) {
        return new GitHubPullRequest(
                random(Integer.class),
                random(String.class),
                random(String.class),
                random(OffsetDateTime.class),
                random(OffsetDateTime.class),
                random(OffsetDateTime.class),
                author
        );
    }

    public static GitHubPullRequest aGitHubPullRequest() {
        return aGitHubPullRequest(
                aGitHubUser()
        );
    }

    public static GitHubUser aGitHubUserAsUser() {
        return new GitHubUser(
                random(String.class),
                "User"
        );
    }

    public static GitHubUser aGitHubUser() {
        return new GitHubUser(
                random(String.class),
                random(String.class)
        );
    }

    public static GitHubPullRequestReview aGitHubPullRequestReview(GitHubUser author) {
        return new GitHubPullRequestReview(
                author,
                random(String.class),
                random(OffsetDateTime.class)
        );
    }

    public static GitHubPullRequestReview aGitHubPullRequestReview() {
        return aGitHubPullRequestReview(
                aGitHubUser()
        );
    }

    public static GitHubWorkflowRun aGitHubWorkflowRun(String conclusion) {
        return new GitHubWorkflowRun(
                random(String.class),
                conclusion,
                random(OffsetDateTime.class),
                random(OffsetDateTime.class),
                random(OffsetDateTime.class)
        );
    }

    public static GitHubDependabotAlert aGitHubDependabotAlert() {
        return aGitHubDependabotAlert(
                aGitHubVulnerability(),
                random(OffsetDateTime.class),
                random(OffsetDateTime.class)
        );
    }

    public static GitHubDependabotAlert aGitHubDependabotAlert(OffsetDateTime fixedAt,
                                                               OffsetDateTime dismissedAt) {
        return aGitHubDependabotAlert(
                aGitHubVulnerability(),
                fixedAt,
                dismissedAt
        );
    }

    public static GitHubDependabotAlert aGitHubDependabotAlert(GitHubVulnerability vulnerability) {
        return aGitHubDependabotAlert(
                vulnerability,
                random(OffsetDateTime.class),
                random(OffsetDateTime.class)
        );
    }

    public static GitHubDependabotAlert aGitHubDependabotAlert(GitHubVulnerability vulnerability,
                                                               OffsetDateTime fixedAt,
                                                               OffsetDateTime dismissedAt) {
        return new GitHubDependabotAlert(
                random(Integer.class),
                random(String.class),
                vulnerability,
                random(OffsetDateTime.class),
                random(OffsetDateTime.class),
                dismissedAt,
                random(String.class),
                random(String.class),
                fixedAt
        );
    }

    public static GitHubVulnerability aGitHubVulnerability(GitHubPackage ghPackage) {
        return new GitHubVulnerability(
                ghPackage,
                random(String.class),
                random(String.class)
        );
    }

    public static GitHubVulnerability aGitHubVulnerability() {
        return aGitHubVulnerability(
                aGitHubPackage()
        );
    }

    public static GitHubPackage aGitHubPackage() {
        return new GitHubPackage(
                random(String.class),
                random(String.class)
        );
    }

    public static GitHubWorkflow aGitHubWorkflow() {
        return new GitHubWorkflow(
                random(Long.class),
                random(String.class)
        );
    }

    public static GitHubWorkflowRun aGitHubWorkflowRun() {
        return new GitHubWorkflowRun(
                random(String.class),
                random(String.class),
                random(OffsetDateTime.class),
                random(OffsetDateTime.class),
                random(OffsetDateTime.class)
        );
    }
}
