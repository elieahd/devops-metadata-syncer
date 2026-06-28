package devops.metadata.syncer.domain.models.randomizers;

import com.devt.randomizer.RandomizerUtils;
import devops.metadata.syncer.domain.models.PullRequestReview;

import java.time.OffsetDateTime;

public class PullRequestReviewRandomizer {

    private final String state;
    private String reviewer;
    private OffsetDateTime publishedAt;

    public PullRequestReviewRandomizer() {
        this.reviewer = RandomizerUtils.random(String.class);
        this.state = RandomizerUtils.random(String.class);
        this.publishedAt = RandomizerUtils.random(OffsetDateTime.class);
    }

    public static PullRequestReviewRandomizer builder() {
        return new PullRequestReviewRandomizer();
    }

    public static PullRequestReview random() {
        return builder().build();
    }

    public PullRequestReviewRandomizer reviewer(String reviewer) {
        this.reviewer = reviewer;
        return this;
    }

    public PullRequestReviewRandomizer publishedAt(OffsetDateTime publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

    public PullRequestReview build() {
        return new PullRequestReview(reviewer, state, publishedAt);
    }

}

