package devops.platform.infrastructure.outbound.github;

import devops.platform.infrastructure.outbound.github.exception.GitHubException;
import devops.platform.infrastructure.outbound.github.models.GitHubDependabotAlert;
import devops.platform.infrastructure.outbound.github.models.GitHubPullRequest;
import devops.platform.infrastructure.outbound.github.models.GitHubPullRequestReview;
import devops.platform.infrastructure.outbound.github.models.GitHubRelease;
import devops.platform.infrastructure.outbound.github.models.GitHubWorkflow;
import devops.platform.infrastructure.outbound.github.models.GitHubWorkflowRun;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class GitHubClientAdapterTest {

    private MockWebServer server;
    private GitHubClientAdapter adapter;

    private String organization;
    private String repository;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        adapter = new GitHubClientAdapter(server.url("/").toString().replaceAll("/$", ""), "test-token");
        organization = "org001";
        repository = "repo002";
    }

    @AfterEach
    void tearDown() {
        server.close();
    }

    @Test
    void findAllPullRequests_shouldReturnPullRequests_whenSinglePageIsReturned() {
        // Arrange
        String body = """
                [
                  {"number": 1, "title": "First PR", "state": "open"},
                  {"number": 2, "title": "Second PR", "state": "closed"}
                ]
                """;
        mock(200, body);
        // Act
        List<GitHubPullRequest> result = adapter.findAllPullRequests(organization, repository, "all");
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).number()).isEqualTo(1);
        assertThat(result.get(0).title()).isEqualTo("First PR");
        assertThat(result.get(0).state()).isEqualTo("open");
        assertThat(result.get(1).number()).isEqualTo(2);
        assertThat(result.get(1).title()).isEqualTo("Second PR");
        assertThat(result.get(1).state()).isEqualTo("closed");
    }

    @Test
    void findAllPullRequests_shouldAggregateAllPages_whenLinkHeaderContainsNextPage() {
        // Arrange
        String page1Url = server.url("/repos/%s/%s/pulls?state=all&per_page=100&sort=created&direction=desc&page=2".formatted(organization, repository)).toString();
        String page1Body = """
                [{"number": 1, "title": "PR 1", "state": "open"}]
                """;
        String page2Body = """
                [{"number": 2, "title": "PR 2", "state": "open"}]
                """;
        mock(200, page1Body, Map.of("Link", "<" + page1Url + ">; rel=\"next\""));
        mock(200, page2Body);
        // Act
        List<GitHubPullRequest> result = adapter.findAllPullRequests(organization, repository, "all");
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(GitHubPullRequest::title).containsExactly("PR 1", "PR 2");
        assertThat(server.getRequestCount()).isEqualTo(2);
    }

    @Test
    void findAllPullRequests_shouldSendAuthorizationAndAcceptHeaders_whenRequestIsMade() throws InterruptedException {
        // Arrange
        mock(200, "[]");
        // Act
        adapter.findAllPullRequests(organization, repository, "open");
        // Assert
        RecordedRequest recordedRequest = server.takeRequest();
        assertThat(recordedRequest.getHeaders().get("Authorization")).isEqualTo("Bearer test-token");
        assertThat(recordedRequest.getHeaders().get("Accept")).isEqualTo("application/vnd.github+json");
        assertThat(recordedRequest.getTarget()).contains("state=open");
    }

    @Test
    void findAllPullRequests_shouldThrowGitHubException_whenResponseIsNotSuccessful() {
        // Arrange
        mock(404, null);
        // Act
        Throwable thrown = catchThrowable(() -> adapter.findAllPullRequests(organization, repository, "all"));
        // Assert
        assertThat(thrown).isInstanceOf(GitHubException.class);
    }

    @Test
    void findAllPullRequests_shouldReturnEmptyList_whenNoPullRequestsExist() {
        // Arrange
        mock(200, "[]");
        // Act
        List<GitHubPullRequest> result = adapter.findAllPullRequests(organization, repository, "all");
        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void findAllPullRequests_shouldThrowGitHubException_whenIOExceptionOccurs() {
        // Arrange
        server.close(); // server no longer listening -> connection failure inside call()
        // Act
        Throwable thrown = catchThrowable(() -> adapter.findAllPullRequests(organization, repository, "all"));
        // Assert
        assertThat(thrown)
                .isInstanceOf(GitHubException.class)
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    void findAllPullRequestReviews_shouldReturnReviews_whenPullRequestNumberIsGiven() throws InterruptedException {
        // Arrange
        String body = """
                [
                  {"id": 10, "state": "APPROVED"},
                  {"id": 11, "state": "CHANGES_REQUESTED"}
                ]
                """;
        mock(200, body);
        // Act
        List<GitHubPullRequestReview> result = adapter.findAllPullRequestReviews(organization, repository, 42L);
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).state()).isEqualTo("APPROVED");
        assertThat(result.get(1).state()).isEqualTo("CHANGES_REQUESTED");
        RecordedRequest recordedRequest = server.takeRequest();
        assertThat(recordedRequest.getTarget()).contains("/repos/%s/%s/pulls/42/reviews".formatted(organization, repository));
    }

    @Test
    void findAllPullRequestReviews_shouldThrowGitHubException_whenCallFails() {
        // Arrange
        mock(500, null);
        // Act
        Throwable thrown = catchThrowable(() -> adapter.findAllPullRequestReviews(organization, repository, 1L));
        // Assert
        assertThat(thrown).isInstanceOf(GitHubException.class);
    }

    @Test
    void findAllWorkflows_shouldReturnUnwrappedWorkflows_whenSinglePageIsReturned() {
        // Arrange
        String body = """
                {
                  "total_count": 2,
                  "workflows": [
                    {"id": 1, "name": "CI"},
                    {"id": 2, "name": "CD"}
                  ]
                }
                """;
        mock(200, body);
        // Act
        List<GitHubWorkflow> result = adapter.findAllWorkflows(organization, repository);
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(GitHubWorkflow::id).containsExactly(1L, 2L);
        assertThat(result).extracting(GitHubWorkflow::name).containsExactly("CI", "CD");
    }

    @Test
    void findAllWorkflows_shouldAggregateWorkflows_whenMultiplePagesAreReturned() {
        // Arrange
        String nextPageUrl = server.url("/repos/%s/%s/actions/workflows?per_page=100&page=2".formatted(organization, repository)).toString();
        String page1 = """
                {"total_count": 2, "workflows": [{"id": 1, "name": "CI"}]}
                """;
        String page2 = """
                {"total_count": 2, "workflows": [{"id": 2, "name": "CD"}]}
                """;
        mock(200, page1, Map.of("Link", "<" + nextPageUrl + ">; rel=\"next\""));
        mock(200, page2);
        // Act
        List<GitHubWorkflow> result = adapter.findAllWorkflows(organization, repository);
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(GitHubWorkflow::id).containsExactly(1L, 2L);
        assertThat(server.getRequestCount()).isEqualTo(2);
    }

    @Test
    void findAllWorkflowRuns_shouldReturnRuns_whenWorkflowIdIsGiven() throws InterruptedException {
        // Arrange
        String body = """
                {
                  "total_count": 1,
                  "workflow_runs": [
                    {"id": 100, "status": "completed"}
                  ]
                }
                """;
        mock(200, body);
        // Act
        List<GitHubWorkflowRun> result = adapter.findAllWorkflowRuns(organization, repository, 7L);
        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo("completed");
        RecordedRequest recordedRequest = server.takeRequest();
        assertThat(recordedRequest.getTarget()).contains("/repos/%s/%s/actions/workflows/7/runs".formatted(organization, repository));
    }

    @Test
    void findAllReleases_shouldReturnReleases_whenSinglePageIsReturned() throws InterruptedException {
        // Arrange
        String body = """
                [
                  {"tag_name": "v1.0.0", "name": "Release 1.0.0", "draft": false, "prerelease": false, "published_at": "2023-01-01T00:00:00Z"},
                  {"tag_name": "v2.0.0", "name": "Release 2.0.0", "draft": true, "prerelease": true, "published_at": "2023-01-01T00:00:00Z"}
                ]
                """;
        mock(200, body);
        // Act
        List<GitHubRelease> result = adapter.findAllReleases(organization, repository);
        // Assert
        assertThat(result)
                .hasSize(2)
                .extracting(GitHubRelease::tagName, GitHubRelease::name, GitHubRelease::draft,
                        GitHubRelease::preRelease, GitHubRelease::publishedAt)
                .containsExactly(
                        tuple("v1.0.0", "Release 1.0.0", false, false, OffsetDateTime.parse("2023-01-01T00:00:00Z")),
                        tuple("v2.0.0", "Release 2.0.0", true, true, OffsetDateTime.parse("2023-01-01T00:00:00Z"))
                );
        assertThat(server.takeRequest().getTarget()).contains("/repos/%s/%s/releases?per_page=100".formatted(organization, repository));
    }

    @Test
    void findAllReleases_shouldThrowGitHubException_whenRequestIsUnauthorized() {
        // Arrange
        mock(401, null);
        // Act
        Throwable thrown = catchThrowable(() -> adapter.findAllReleases(organization, repository));
        // Assert
        assertThat(thrown).isInstanceOf(GitHubException.class);
    }

    @Test
    void findAllDependabotAlerts_shouldReturnAlerts_whenAlertsExist() throws InterruptedException {
        // Arrange
        String body = """
                [
                  {"number": 1, "state": "open", "created_at": "2023-01-01T00:00:00Z", "updated_at": "2023-01-02T00:00:00Z", "dismissed_at": null, "dismissed_reason": null, "dismissed_comment": null, "fixed_at": null},
                  {"number": 2, "state": "fixed", "created_at": "2023-02-01T00:00:00Z", "updated_at": "2023-02-02T00:00:00Z", "dismissed_at": null, "dismissed_reason": null, "dismissed_comment": null, "fixed_at": "2023-02-03T00:00:00Z"}
                ]
                """;
        mock(200, body);
        // Act
        List<GitHubDependabotAlert> result = adapter.findAllDependabotAlerts(organization, repository);
        // Assert
        assertThat(result)
                .hasSize(2)
                .extracting(GitHubDependabotAlert::number, GitHubDependabotAlert::state,
                        GitHubDependabotAlert::createdAt, GitHubDependabotAlert::updatedAt,
                        GitHubDependabotAlert::dismissedAt, GitHubDependabotAlert::dismissedReason,
                        GitHubDependabotAlert::dismissedComment, GitHubDependabotAlert::fixedAt)
                .containsExactly(
                        tuple(1, "open", OffsetDateTime.parse("2023-01-01T00:00:00Z"),
                                OffsetDateTime.parse("2023-01-02T00:00:00Z"), null, null, null, null),
                        tuple(2, "fixed", OffsetDateTime.parse("2023-02-01T00:00:00Z"),
                                OffsetDateTime.parse("2023-02-02T00:00:00Z"), null, null, null,
                                OffsetDateTime.parse("2023-02-03T00:00:00Z"))
                );
        assertThat(server.takeRequest().getTarget()).contains("/repos/%s/%s/dependabot/alerts".formatted(organization, repository));
    }

    @Test
    void findAllDependabotAlerts_shouldReturnEmptyList_whenNoAlertsExist() {
        // Arrange
        mock(200, "[]");
        // Act
        List<GitHubDependabotAlert> result = adapter.findAllDependabotAlerts(organization, repository);
        // Assert
        assertThat(result).isEmpty();
    }

    private void mock(int code, String body) {
        mock(code, body, Map.of());
    }

    private void mock(int code, String body, Map<String, String> headers) {
        MockResponse.Builder responseBuilder = new MockResponse.Builder().code(code);
        if (body != null) {
            responseBuilder.body(body).addHeader("Content-Type", "application/json");
        }
        headers.forEach(responseBuilder::addHeader);
        server.enqueue(responseBuilder.build());
    }

}