package devops.platform.domain.models.randomizers;

import com.devt.randomizer.RandomizerUtils;
import devops.platform.domain.models.PullRequest;
import devops.platform.domain.models.PullRequestReview;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.IntStream;

public class PullRequestRandomizer {

    private final int number;
    private final String title;
    private final String state;
    private final boolean isAuthorUser;
    private OffsetDateTime publishedAt;
    private OffsetDateTime mergedAt;
    private OffsetDateTime closedAt;
    private String author;
    private List<PullRequestReview> reviews;

    public PullRequestRandomizer() {
        this.number = RandomizerUtils.random(Integer.class);
        this.title = RandomizerUtils.random(String.class);
        this.state = RandomizerUtils.random(String.class);
        this.publishedAt = RandomizerUtils.random(OffsetDateTime.class);
        this.mergedAt = RandomizerUtils.random(OffsetDateTime.class);
        this.closedAt = RandomizerUtils.random(OffsetDateTime.class);
        this.author = RandomizerUtils.random(String.class);
        this.isAuthorUser = RandomizerUtils.random(Boolean.class);
        this.reviews = IntStream.range(0, 5).mapToObj(_ -> PullRequestReviewRandomizer.random()).toList();
    }

    public static PullRequestRandomizer builder() {
        return new PullRequestRandomizer();
    }

    public static PullRequest random() {
        return builder().build();
    }

    public PullRequestRandomizer publishedAt(OffsetDateTime publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

    public PullRequestRandomizer mergedAt(OffsetDateTime mergedAt) {
        this.mergedAt = mergedAt;
        return this;
    }

    public PullRequestRandomizer closedAt(OffsetDateTime closedAt) {
        this.closedAt = closedAt;
        return this;
    }

    public PullRequestRandomizer author(String author) {
        this.author = author;
        return this;
    }

    public PullRequestRandomizer reviews(List<PullRequestReview> reviews) {
        this.reviews = reviews;
        return this;
    }

    public PullRequest build() {
        return new PullRequest(
                number,
                title,
                state,
                publishedAt,
                mergedAt,
                closedAt,
                author,
                isAuthorUser,
                reviews
        );
    }

}

