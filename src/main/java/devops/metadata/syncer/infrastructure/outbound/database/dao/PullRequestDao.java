package devops.metadata.syncer.infrastructure.outbound.database.dao;

import devops.metadata.syncer.domain.models.PullRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PullRequestDao {

    void deleteAllByRepositoryId(@Param("repositoryId") Long repositoryId);

    void insertAll(@Param("repositoryId") Long repositoryId,
                   @Param("pullRequests") List<PullRequest> pullRequests);

    List<PullRequest> findAllByRepositoryId(@Param("repositoryId") Long repositoryId);

    Long findIdByRepositoryIdAndNumber(@Param("repositoryId") Long repositoryId,
                                       @Param("pullRequestNumber") Integer pullRequestNumber);
}
