package devops.metadata.syncer.infrastructure.outbound.synchronizers.github.mappers;

import devops.metadata.syncer.domain.models.PullRequest;
import devops.metadata.syncer.domain.models.PullRequestReview;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubPullRequest;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubPullRequestReview;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubUser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GitHubPullRequestMapper implements Mapper {

    public PullRequest map(GitHubPullRequest pullRequest, List<GitHubPullRequestReview> ghReviews) {
        if (pullRequest == null) {
            return null;
        }

        List<PullRequestReview> reviews = map(ghReviews, this::map);

        return new PullRequest(
                pullRequest.number(),
                pullRequest.title(),
                pullRequest.state(),
                pullRequest.createdAt(),
                pullRequest.mergedAt(),
                pullRequest.closedAt(),
                mapUser(pullRequest.user()),
                checkIfAuthorUser(pullRequest.user()),
                reviews
        );
    }

    private String mapUser(GitHubUser user) {
        if (user == null) {
            return null;
        }
        return user.login();
    }

    private boolean checkIfAuthorUser(GitHubUser user) {
        if (user == null) {
            return false;
        }
        return user.isUser();
    }

    private PullRequestReview map(GitHubPullRequestReview review) {
        return new PullRequestReview(
                mapUser(review.user()),
                review.state(),
                review.submittedAt()
        );
    }

}
