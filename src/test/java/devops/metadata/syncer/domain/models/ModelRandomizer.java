package devops.metadata.syncer.domain.models;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.devt.randomizer.RandomizerUtils.random;

public class ModelRandomizer {

    private ModelRandomizer() {
        // utility class shouldn't be instantiated
    }

    public static RepositorySource aRepositorySource() {
        return random(RepositorySource.class);
    }

    public static Project aProject() {
        return new Project(
                random(Long.class),
                random(String.class),
                random(String.class)
        );
    }

    public static Repository aRepository() {
        return aRepository(
                aRepositorySource()
        );
    }

    public static Repository aRepository(RepositorySource source) {
        return aRepository(
                source,
                random(LocalDateTime.class)
        );
    }

    public static Repository aRepository(RepositorySource source,
                                         LocalDateTime lastSyncTime) {
        return new Repository(
                random(Long.class),
                random(String.class),
                random(String.class),
                source,
                lastSyncTime
        );
    }

    public static Pipeline aPipeline(List<PipelineRun> runs) {
        return new Pipeline(
                random(String.class),
                random(String.class),
                runs
        );
    }

    public static Pipeline aPipeline() {
        return new Pipeline(
                random(String.class),
                random(String.class),
                new ArrayList<>()
        );
    }

    public static PipelineRun aPipelineRun() {
        return aPipelineRun(
                random(OffsetDateTime.class),
                random(OffsetDateTime.class)
        );
    }

    public static PipelineRun aPipelineRun(OffsetDateTime startedAt,
                                           OffsetDateTime updatedAt) {
        return new PipelineRun(
                random(Boolean.class),
                startedAt,
                random(OffsetDateTime.class),
                updatedAt
        );
    }

    public static PullRequestReview aPullRequestReview(String author,
                                                       OffsetDateTime submittedAt) {
        return new PullRequestReview(
                author,
                random(String.class),
                submittedAt
        );
    }

    public static PullRequestReview aPullRequestReview() {
        return aPullRequestReview(
                random(String.class),
                random(OffsetDateTime.class)
        );
    }

    public static PullRequest aPullRequest(OffsetDateTime mergedAt) {
        return new PullRequest(
                random(Integer.class),
                random(String.class),
                random(String.class),
                random(OffsetDateTime.class),
                mergedAt,
                random(OffsetDateTime.class),
                random(String.class),
                random(Boolean.class),
                IntStream.range(0, 5).mapToObj(_ -> aPullRequestReview()).toList()
        );
    }

    public static PullRequest aPullRequest(OffsetDateTime mergedAt,
                                           String author,
                                           List<PullRequestReview> reviews) {
        return new PullRequest(
                random(Integer.class),
                random(String.class),
                random(String.class),
                random(OffsetDateTime.class),
                mergedAt,
                random(OffsetDateTime.class),
                author,
                random(Boolean.class),
                reviews
        );
    }

    public static PullRequest aPullRequest() {
        return aPullRequest(
                random(OffsetDateTime.class),
                random(String.class),
                IntStream.range(0, 5).mapToObj(_ -> aPullRequestReview()).toList()
        );
    }

    public static PullRequest aPullRequest(OffsetDateTime publishedAt,
                                           OffsetDateTime mergedAt,
                                           OffsetDateTime closedAt) {
        return new PullRequest(
                random(Integer.class),
                random(String.class),
                random(String.class),
                publishedAt,
                mergedAt,
                closedAt,
                random(String.class),
                random(Boolean.class),
                IntStream.range(0, 5).mapToObj(_ -> aPullRequestReview()).toList()
        );
    }

    public static PullRequest aPullRequest(List<PullRequestReview> reviews) {
        return new PullRequest(
                random(Integer.class),
                random(String.class),
                random(String.class),
                random(OffsetDateTime.class),
                random(OffsetDateTime.class),
                random(OffsetDateTime.class),
                random(String.class),
                random(Boolean.class),
                reviews
        );
    }

    public static Vulnerability aVulnerability() {
        return new Vulnerability(
                random(String.class),
                random(String.class),
                random(String.class),
                random(OffsetDateTime.class),
                random(OffsetDateTime.class)
        );
    }

    public static Release aRelease() {
        return new Release(
                random(String.class),
                random(String.class),
                random(OffsetDateTime.class)
        );
    }
}
