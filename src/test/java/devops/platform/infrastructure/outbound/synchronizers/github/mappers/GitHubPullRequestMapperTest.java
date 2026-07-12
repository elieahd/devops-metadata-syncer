package devops.platform.infrastructure.outbound.synchronizers.github.mappers;

import devops.platform.domain.models.PullRequest;
import devops.platform.domain.models.PullRequestReview;
import devops.platform.infrastructure.outbound.github.models.GitHubPullRequest;
import devops.platform.infrastructure.outbound.github.models.GitHubPullRequestReview;
import devops.platform.infrastructure.outbound.github.models.GitHubUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.devt.randomizer.RandomizerUtils.random;
import static devops.platform.infrastructure.outbound.github.models.GitHubModelRandomizer.aGitHubPullRequest;
import static devops.platform.infrastructure.outbound.github.models.GitHubModelRandomizer.aGitHubPullRequestReview;
import static devops.platform.infrastructure.outbound.github.models.GitHubModelRandomizer.aGitHubUser;
import static org.assertj.core.api.Assertions.assertThat;

class GitHubPullRequestMapperTest {

    private GitHubPullRequestMapper sut;

    @BeforeEach
    void setUp() {
        sut = new GitHubPullRequestMapper();
    }

    @Test
    void map_shouldReturnNull_whenPullRequestIsNull() {
        // Arrange
        GitHubPullRequest gitHubPR = null;
        List<GitHubPullRequestReview> githubPrReviews = new ArrayList<>();
        // Act
        PullRequest pr = sut.map(gitHubPR, githubPrReviews);
        // Assert
        assertThat(pr).isNull();
    }

    @Test
    void map_shouldMapNumber() {
        // Arrange
        GitHubPullRequest gitHubPR = aGitHubPullRequest();
        List<GitHubPullRequestReview> githubPrReviews = new ArrayList<>();
        // Act
        PullRequest pr = sut.map(gitHubPR, githubPrReviews);
        // Assert
        assertThat(pr).isNotNull();
        assertThat(pr.number()).isEqualTo(gitHubPR.number());
        assertThat(pr.title()).isEqualTo(gitHubPR.title());
        assertThat(pr.state()).isEqualTo(gitHubPR.state());
        assertThat(pr.publishedAt()).isEqualTo(gitHubPR.createdAt());
        assertThat(pr.mergedAt()).isEqualTo(gitHubPR.mergedAt());
        assertThat(pr.closedAt()).isEqualTo(gitHubPR.closedAt());
    }

    @Test
    void map_shouldMapUser() {
        // Arrange
        GitHubPullRequest gitHubPR = aGitHubPullRequest();
        List<GitHubPullRequestReview> githubPrReviews = new ArrayList<>();
        // Act
        PullRequest pr = sut.map(gitHubPR, githubPrReviews);
        // Assert
        assertThat(pr).isNotNull();
        assertThat(pr.author()).isEqualTo(gitHubPR.user().login());
    }

    @Test
    void map_shouldMapUserToNull_whenUserIsNull() {
        // Arrange
        GitHubUser gitHubUser = null;
        GitHubPullRequest gitHubPR = aGitHubPullRequest(gitHubUser);
        List<GitHubPullRequestReview> githubPrReviews = new ArrayList<>();
        // Act
        PullRequest pr = sut.map(gitHubPR, githubPrReviews);
        // Assert
        assertThat(pr).isNotNull();
        assertThat(pr.author()).isNull();
    }

    @Test
    void map_shouldMapUserToNull_whenUserLoginIsNull() {
        // Arrange
        GitHubUser gitHubUser = new GitHubUser(null, random(String.class));
        GitHubPullRequest gitHubPR = aGitHubPullRequest(gitHubUser);
        List<GitHubPullRequestReview> githubPrReviews = new ArrayList<>();
        // Act
        PullRequest pr = sut.map(gitHubPR, githubPrReviews);
        // Assert
        assertThat(pr).isNotNull();
        assertThat(pr.author()).isNull();
    }

    @Test
    void map_shouldMapIsAuthorUserToTrue_whenUserIsAuthor() {
        // Arrange
        GitHubUser gitHubUser = new GitHubUser(random(String.class), "User");
        GitHubPullRequest gitHubPR = aGitHubPullRequest(gitHubUser);
        List<GitHubPullRequestReview> githubPrReviews = new ArrayList<>();
        // Act
        PullRequest pr = sut.map(gitHubPR, githubPrReviews);
        // Assert
        assertThat(pr).isNotNull();
        assertThat(pr.isAuthorUser()).isTrue();
    }

    @Test
    void map_shouldMapIsAuthorUserToFalse_whenUserIsNotAuthor() {
        // Arrange
        GitHubUser gitHubUser = aGitHubUser();
        GitHubPullRequest gitHubPR = aGitHubPullRequest(gitHubUser);
        List<GitHubPullRequestReview> githubPrReviews = new ArrayList<>();
        // Act
        PullRequest pr = sut.map(gitHubPR, githubPrReviews);
        // Assert
        assertThat(pr).isNotNull();
        assertThat(pr.isAuthorUser()).isFalse();
    }

    @Test
    void map_shouldMapIsAuthorUserToFalse_whenUserIsNull() {
        // Arrange
        GitHubUser gitHubUser = null;
        GitHubPullRequest gitHubPR = aGitHubPullRequest(gitHubUser);
        List<GitHubPullRequestReview> githubPrReviews = new ArrayList<>();
        // Act
        PullRequest pr = sut.map(gitHubPR, githubPrReviews);
        // Assert
        assertThat(pr).isNotNull();
        assertThat(pr.isAuthorUser()).isFalse();
    }

    @Test
    void map_shouldMapReviewsToEmptyList_whenReviewsIsNull() {
        // Arrange
        GitHubPullRequest gitHubPR = aGitHubPullRequest();
        List<GitHubPullRequestReview> githubPrReviews = null;
        // Act
        PullRequest pr = sut.map(gitHubPR, githubPrReviews);
        // Assert
        assertThat(pr).isNotNull();
        assertThat(pr.reviews())
                .isNotNull()
                .isEmpty();
    }

    @Test
    void map_shouldMapReviewsToEmptyList_whenReviewsIsEmpty() {
        // Arrange
        GitHubPullRequest gitHubPR = aGitHubPullRequest();
        List<GitHubPullRequestReview> githubPrReviews = new ArrayList<>();
        // Act
        PullRequest pr = sut.map(gitHubPR, githubPrReviews);
        // Assert
        assertThat(pr).isNotNull();
        assertThat(pr.reviews())
                .isNotNull()
                .isEmpty();
    }

    @Test
    void map_shouldMapReviews() {
        // Arrange
        GitHubPullRequest gitHubPR = aGitHubPullRequest();

        GitHubPullRequestReview githubReview1 = aGitHubPullRequestReview();
        GitHubPullRequestReview githubReview2 = aGitHubPullRequestReview(null);

        List<GitHubPullRequestReview> githubPrReviews = List.of(
                githubReview1,
                githubReview2
        );
        // Act
        PullRequest pr = sut.map(gitHubPR, githubPrReviews);
        // Assert
        assertThat(pr).isNotNull();
        assertThat(pr.reviews())
                .isNotNull()
                .isNotEmpty()
                .hasSameSizeAs(githubPrReviews);

        PullRequestReview review1 = pr.reviews().getFirst();
        assertThat(review1).isNotNull();
        assertThat(review1.reviewer()).isEqualTo(githubReview1.user().login());
        assertThat(review1.state()).isEqualTo(githubReview1.state());
        assertThat(review1.submittedAt()).isEqualTo(githubReview1.submittedAt());

        PullRequestReview review2 = pr.reviews().get(1);
        assertThat(review2).isNotNull();
        assertThat(review2.reviewer()).isNull();
        assertThat(review2.state()).isEqualTo(githubReview2.state());
        assertThat(review2.submittedAt()).isEqualTo(githubReview2.submittedAt());
    }


}
