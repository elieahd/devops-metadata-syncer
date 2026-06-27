package devops.metadata.syncer.domain.outbound;

import devops.metadata.syncer.domain.models.PullRequest;

import java.util.List;

public interface PullRequestInventory {

    void deleteAllByRepositoryId(Long repositoryId);

    void insertAll(Long repositoryId,
                   List<PullRequest> pullRequests);

    List<PullRequest> findAllByRepositoryId(Long repositoryId);

}
