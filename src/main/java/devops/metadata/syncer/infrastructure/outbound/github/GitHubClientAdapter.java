package devops.metadata.syncer.infrastructure.outbound.github;

import devops.metadata.syncer.infrastructure.outbound.github.exception.GitHubException;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubDependabotAlert;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubPullRequest;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubPullRequestReview;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubRelease;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubResponse;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubWorkflow;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubWorkflowList;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubWorkflowRun;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubWorkflowRunList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class GitHubClientAdapter implements GitHubClient {

    private static final int PAGE_SIZE = 100;

    private final String baseUrl;
    private final String token;
    private final OkHttpClient httpClient;
    private final JsonMapper mapper;

    public GitHubClientAdapter(String baseUrl, String token) {
        this.baseUrl = baseUrl;
        this.token = token;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        this.mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    @Override
    public List<GitHubPullRequest> findAllPullRequests(String organization,
                                                       String repository,
                                                       String state) {
        String url = "%s/repos/%s/%s/pulls?state=%s&per_page=%d&sort=created&direction=desc".formatted(baseUrl, organization, repository, state, PAGE_SIZE);
        return callUntilLastPage(url, new TypeReference<>() {
        });
    }

    @Override
    public List<GitHubPullRequestReview> findAllPullRequestReviews(String organization,
                                                                   String repository,
                                                                   long pullRequestNumber) {
        String url = "%s/repos/%s/%s/pulls/%d/reviews".formatted(this.baseUrl, organization, repository, pullRequestNumber);
        GitHubResponse<List<GitHubPullRequestReview>> response = call(url, new TypeReference<>() {
        });
        return response.data();
    }

    @Override
    public List<GitHubWorkflow> findAllWorkflows(String organization,
                                                 String repository) {
        String url = "%s/repos/%s/%s/actions/workflows?per_page=%d".formatted(baseUrl, organization, repository, PAGE_SIZE);
        return callUntilLastPageWrapped(url, new TypeReference<>() {
        }, GitHubWorkflowList::workflows);
    }

    @Override
    public List<GitHubWorkflowRun> findAllWorkflowRuns(String organization,
                                                       String repository,
                                                       long workflowId) {
        String url = "%s/repos/%s/%s/actions/workflows/%d/runs?per_page=%d&sort=created&direction=desc"
                .formatted(baseUrl, organization, repository, workflowId, PAGE_SIZE);
        return callUntilLastPageWrapped(url, new TypeReference<>() {
        }, GitHubWorkflowRunList::runs);
    }

    @Override
    public List<GitHubRelease> findAllReleases(String organization, String repository) {
        String url = "%s/repos/%s/%s/releases?per_page=%d".formatted(baseUrl, organization, repository, PAGE_SIZE);
        return callUntilLastPage(url, new TypeReference<>() {
        });
    }

    @Override
    public List<GitHubDependabotAlert> findAllDependabotAlerts(String organization, String repository) {
        String url = "%s/repos/%s/%s/dependabot/alerts?per_page=%d".formatted(baseUrl, organization, repository, PAGE_SIZE);
        return callUntilLastPage(url, new TypeReference<>() {
        });
    }

    // -------------------------------------------------------------------------
    // Pagination helpers
    // -------------------------------------------------------------------------

    private <W, T> List<T> callUntilLastPageWrapped(String url,
                                                    TypeReference<W> typeReference,
                                                    Function<W, List<T>> unwrap) {
        List<T> results = new ArrayList<>();
        while (url != null) {
            GitHubResponse<W> page = call(url, typeReference);
            if (page.data() != null) {
                results.addAll(unwrap.apply(page.data()));
            }
            url = page.nextUrl();
        }
        return results;
    }

    private <T> List<T> callUntilLastPage(String url, TypeReference<List<T>> typeReference) {
        return callUntilLastPageWrapped(url, typeReference, w -> w);
    }

    // -------------------------------------------------------------------------
    // HTTP
    // -------------------------------------------------------------------------

    private <T> GitHubResponse<T> call(String url, TypeReference<T> typeReference) {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/vnd.github+json")
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new GitHubException(response.code(), response.message());
            }
            if (response.body() == null) {
                return new GitHubResponse<>(null, null);
            }
            String json = response.body().string();
            T data = mapper.readValue(json, typeReference);
            return new GitHubResponse<>(data, nextUrl(response));
        } catch (IOException e) {
            throw new GitHubException(e);
        }
    }

    private String nextUrl(Response response) {
        String link = response.header("Link");
        if (link == null) {
            return null;
        }
        for (String part : link.split(",")) {
            String[] pieces = part.split(";");
            if (pieces.length < 2) {
                continue;
            }
            String url = pieces[0].trim();
            String rel = pieces[1].trim();
            if (rel.equals("rel=\"next\"")) {
                return url.substring(1, url.length() - 1);
            }
        }
        return null;
    }

}