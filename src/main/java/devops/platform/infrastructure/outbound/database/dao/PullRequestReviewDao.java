package devops.platform.infrastructure.outbound.database.dao;

import devops.platform.domain.models.PullRequestReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PullRequestReviewDao {

    List<PullRequestReview> findAllByPullRequestId(@Param("pullRequestId") Long pullRequestId);

    void deleteAllByRepositoryId(@Param("repositoryId") Long repositoryId);

    void insertAll(@Param("pullRequestId") Long pullRequestId,
                   @Param("reviews") List<PullRequestReview> reviews);
}
