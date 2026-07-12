package devops.platform.infrastructure.outbound.synchronizers.github;

import devops.platform.domain.models.Pipeline;
import devops.platform.domain.models.PipelineRun;
import devops.platform.domain.models.PullRequest;
import devops.platform.domain.models.PullRequestReview;
import devops.platform.domain.models.Release;
import devops.platform.domain.models.Vulnerability;
import devops.platform.infrastructure.outbound.github.models.GitHubDependabotAlert;
import devops.platform.infrastructure.outbound.github.models.GitHubPackage;
import devops.platform.infrastructure.outbound.github.models.GitHubPullRequest;
import devops.platform.infrastructure.outbound.github.models.GitHubPullRequestReview;
import devops.platform.infrastructure.outbound.github.models.GitHubRelease;
import devops.platform.infrastructure.outbound.github.models.GitHubUser;
import devops.platform.infrastructure.outbound.github.models.GitHubVulnerability;
import devops.platform.infrastructure.outbound.github.models.GitHubWorkflow;
import devops.platform.infrastructure.outbound.github.models.GitHubWorkflowRun;
import devops.platform.infrastructure.outbound.synchronizers.github.mappers.GitHubPullRequestMapper;
import devops.platform.infrastructure.outbound.synchronizers.github.mappers.GitHubReleaseMapper;
import devops.platform.infrastructure.outbound.synchronizers.github.mappers.GitHubVulnerabilityMapper;
import devops.platform.infrastructure.outbound.synchronizers.github.mappers.GitHubWorkflowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class GithubRepositoryInventoryTest {

    private FakeGitHubClient gitHubClient;
    private GithubRepositoryInventory inventory;

    private String organization;
    private String repository;

    @BeforeEach
    void setUp() {
        gitHubClient = new FakeGitHubClient();
        inventory = new GithubRepositoryInventory(
                gitHubClient,
                new GitHubPullRequestMapper(),
                new GitHubWorkflowMapper(),
                new GitHubReleaseMapper(),
                new GitHubVulnerabilityMapper()
        ) {
        };
        organization = random(String.class);
        repository = random(String.class);
    }

    @Test
    void findAllPullRequests_shouldMapPullRequestsWithReviews_whenClosedPullRequestsExist() {
        // Arrange
        GitHubUser author = new GitHubUser("alice", "User");
        GitHubUser reviewer = new GitHubUser("bob", "User");

        OffsetDateTime createdAt = OffsetDateTime.parse("2023-01-01T00:00:00Z");
        OffsetDateTime mergedAt = OffsetDateTime.parse("2023-01-02T00:00:00Z");
        OffsetDateTime submittedAt = OffsetDateTime.parse("2023-01-01T12:00:00Z");

        GitHubPullRequest pr1 = new GitHubPullRequest(1, "PR 1", "closed", createdAt, mergedAt, null, author);
        gitHubClient.pullRequestsByState.put("closed", List.of(pr1));
        gitHubClient.reviewsByPrNumber.put(1L, List.of(new GitHubPullRequestReview(reviewer, "APPROVED", submittedAt)));
        // Act
        List<PullRequest> result = inventory.findAllPullRequests(organization, repository);
        // Assert
        assertThat(result).hasSize(1);
        PullRequest mapped = result.getFirst();
        assertThat(mapped.number()).isEqualTo(1);
        assertThat(mapped.title()).isEqualTo("PR 1");
        assertThat(mapped.state()).isEqualTo("closed");
        assertThat(mapped.publishedAt()).isEqualTo(createdAt);
        assertThat(mapped.mergedAt()).isEqualTo(mergedAt);
        assertThat(mapped.closedAt()).isNull();
        assertThat(mapped.author()).isEqualTo("alice");
        assertThat(mapped.isAuthorUser()).isTrue();
        assertThat(mapped.reviews())
                .extracting(PullRequestReview::reviewer, PullRequestReview::state, PullRequestReview::submittedAt)
                .containsExactly(tuple("bob", "APPROVED", submittedAt));
        assertThat(gitHubClient.requestedOrganization).isEqualTo(organization);
        assertThat(gitHubClient.requestedRepository).isEqualTo(repository);
        assertThat(gitHubClient.requestedState).isEqualTo("closed");
    }

    @Test
    void findAllPullRequests_shouldMapAuthorAsNullAndNotUser_whenUserIsAbsent() {
        // Arrange
        GitHubPullRequest pr1 = new GitHubPullRequest(1, "PR 1", "closed", null, null, null, null);
        gitHubClient.pullRequestsByState.put("closed", List.of(pr1));
        gitHubClient.reviewsByPrNumber.put(1L, List.of());
        // Act
        List<PullRequest> result = inventory.findAllPullRequests(organization, repository);
        // Assert
        assertThat(result).hasSize(1);
        PullRequest pr = result.getFirst();
        assertThat(pr.author()).isNull();
        assertThat(pr.isAuthorUser()).isFalse();
        assertThat(pr.reviews()).isEmpty();
    }

    @Test
    void findAllPullRequests_shouldReturnEmptyList_whenNoPullRequestsExist() {
        // Arrange
        gitHubClient.pullRequestsByState.put("closed", List.of());
        // Act
        List<PullRequest> result = inventory.findAllPullRequests(organization, repository);
        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void findAllWorkflows_shouldMapWorkflowsWithRuns_whenWorkflowsExist() {
        // Arrange
        GitHubWorkflow workflow = new GitHubWorkflow(1L, "CI");
        gitHubClient.workflows = List.of(workflow);

        OffsetDateTime startedAt = OffsetDateTime.parse("2023-01-01T00:00:00Z");
        OffsetDateTime createdAt = OffsetDateTime.parse("2023-01-01T00:01:00Z");
        OffsetDateTime updatedAt = OffsetDateTime.parse("2023-01-01T00:10:00Z");
        GitHubWorkflowRun run = new GitHubWorkflowRun("completed", "success", startedAt, createdAt, updatedAt);
        gitHubClient.runsByWorkflowId.put(1L, List.of(run));
        // Act
        List<Pipeline> result = inventory.findAllWorkflows(organization, repository);
        // Assert
        assertThat(result).hasSize(1);
        Pipeline mapped = result.getFirst();
        assertThat(mapped.name()).isEqualTo("CI");
        assertThat(mapped.sourceId()).isEqualTo("1");
        assertThat(mapped.runs())
                .extracting(PipelineRun::success, PipelineRun::startedAt, PipelineRun::createdAt, PipelineRun::updatedAt)
                .containsExactly(tuple(true, startedAt, createdAt, updatedAt));
        assertThat(gitHubClient.requestedOrganization).isEqualTo(organization);
        assertThat(gitHubClient.requestedRepository).isEqualTo(repository);
    }

    @Test
    void findAllWorkflows_shouldMapRunAsNotSuccessful_whenConclusionIsNotSuccess() {
        // Arrange
        GitHubWorkflow workflow = new GitHubWorkflow(1L, "CI");
        gitHubClient.workflows = List.of(workflow);
        GitHubWorkflowRun run = new GitHubWorkflowRun("completed", "failure", null, null, null);
        gitHubClient.runsByWorkflowId.put(1L, List.of(run));
        // Act
        List<Pipeline> result = inventory.findAllWorkflows(organization, repository);
        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().runs()).extracting(PipelineRun::success).containsExactly(false);
    }

    @Test
    void findAllWorkflows_shouldReturnEmptyList_whenNoWorkflowsExist() {
        // Arrange
        gitHubClient.workflows = List.of();
        // Act
        List<Pipeline> result = inventory.findAllWorkflows(organization, repository);
        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void findAllReleases_shouldExcludeDraftAndPreReleaseAndSortByPublishedAt_whenReleasesExist() {
        // Arrange
        GitHubRelease draftRelease = new GitHubRelease("v0.1.0", "Draft", true, false, OffsetDateTime.parse("2023-01-01T00:00:00Z"));
        GitHubRelease preRelease = new GitHubRelease("v0.2.0", "Beta", false, true, OffsetDateTime.parse("2023-01-02T00:00:00Z"));
        GitHubRelease olderRelease = new GitHubRelease("v1.0.0", "Stable 1.0.0", false, false, OffsetDateTime.parse("2023-01-03T00:00:00Z"));
        GitHubRelease newerRelease = new GitHubRelease("v2.0.0", "Stable 2.0.0", false, false, OffsetDateTime.parse("2023-02-01T00:00:00Z"));
        gitHubClient.releases = List.of(draftRelease, preRelease, newerRelease, olderRelease);
        // Act
        List<Release> result = inventory.findAllReleases(organization, repository);
        // Assert
        assertThat(result)
                .extracting(Release::tagName, Release::name, Release::publishedAt)
                .containsExactly(
                        tuple("v1.0.0", "Stable 1.0.0", OffsetDateTime.parse("2023-01-03T00:00:00Z")),
                        tuple("v2.0.0", "Stable 2.0.0", OffsetDateTime.parse("2023-02-01T00:00:00Z"))
                );
    }

    @Test
    void findAllReleases_shouldReturnEmptyList_whenOnlyDraftAndPreReleasesExist() {
        // Arrange
        GitHubRelease draftRelease = new GitHubRelease("v0.1.0", "Draft", true, false, OffsetDateTime.parse("2023-01-01T00:00:00Z"));
        GitHubRelease preRelease = new GitHubRelease("v0.2.0", "Beta", false, true, OffsetDateTime.parse("2023-01-02T00:00:00Z"));
        gitHubClient.releases = List.of(draftRelease, preRelease);
        // Act
        List<Release> result = inventory.findAllReleases(organization, repository);
        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void findAllVulnerabilities_shouldMapAlertsResolvedByFixedAt_whenFixedAtIsPresent() {
        // Arrange
        GitHubPackage gitHubPackage = new GitHubPackage("npm", "lodash");
        GitHubVulnerability vulnerability = new GitHubVulnerability(gitHubPackage, "HIGH", "< 4.17.21");
        OffsetDateTime createdAt = OffsetDateTime.parse("2023-01-01T00:00:00Z");
        OffsetDateTime fixedAt = OffsetDateTime.parse("2023-01-05T00:00:00Z");
        OffsetDateTime dismissedAt = OffsetDateTime.parse("2023-01-03T00:00:00Z");

        GitHubDependabotAlert alert = new GitHubDependabotAlert(1, "fixed", vulnerability,
                createdAt, null, dismissedAt, null, null, fixedAt);
        gitHubClient.dependabotAlerts = List.of(alert);
        // Act
        List<Vulnerability> result = inventory.findAllVulnerabilities(organization, repository);
        // Assert
        assertThat(result).hasSize(1);
        Vulnerability mapped = result.getFirst();
        assertThat(mapped.artifact()).isEqualTo("lodash");
        assertThat(mapped.impactedVersion()).isEqualTo("< 4.17.21");
        assertThat(mapped.state()).isEqualTo("fixed");
        assertThat(mapped.createdAt()).isEqualTo(createdAt);
        assertThat(mapped.resolvedAt()).isEqualTo(fixedAt);
    }

    @Test
    void findAllVulnerabilities_shouldMapAlertResolvedByDismissedAt_whenFixedAtIsAbsent() {
        // Arrange
        GitHubPackage gitHubPackage = new GitHubPackage("npm", "lodash");
        GitHubVulnerability vulnerability = new GitHubVulnerability(gitHubPackage, "HIGH", "< 4.17.21");
        OffsetDateTime dismissedAt = OffsetDateTime.parse("2023-01-03T00:00:00Z");

        GitHubDependabotAlert alert = new GitHubDependabotAlert(1, "dismissed", vulnerability,
                OffsetDateTime.parse("2023-01-01T00:00:00Z"), null, dismissedAt, "false_positive", null, null);
        gitHubClient.dependabotAlerts = List.of(alert);
        // Act
        List<Vulnerability> result = inventory.findAllVulnerabilities(organization, repository);
        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().resolvedAt()).isEqualTo(dismissedAt);
    }

    @Test
    void findAllVulnerabilities_shouldFilterOutAlert_whenSecurityVulnerabilityIsAbsent() {
        // Arrange
        GitHubDependabotAlert alertWithoutVulnerability = new GitHubDependabotAlert(1, "open", null,
                OffsetDateTime.parse("2023-01-01T00:00:00Z"), null, null, null, null, null);
        gitHubClient.dependabotAlerts = List.of(alertWithoutVulnerability);
        // Act
        List<Vulnerability> result = inventory.findAllVulnerabilities(organization, repository);
        // Assert
        assertThat(result).containsOnlyNulls();
    }

    @Test
    void findAllVulnerabilities_shouldReturnEmptyList_whenNoAlertsExist() {
        // Arrange
        gitHubClient.dependabotAlerts = List.of();
        // Act
        List<Vulnerability> result = inventory.findAllVulnerabilities(organization, repository);
        // Assert
        assertThat(result).isEmpty();
    }
}