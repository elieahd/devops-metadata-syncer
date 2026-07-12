package devops.platform.infrastructure.outbound.database;

import devops.platform.domain.models.PullRequest;
import devops.platform.domain.outbound.PullRequestInventory;
import devops.platform.infrastructure.outbound.OutboundAdapter;
import devops.platform.infrastructure.outbound.database.dao.PullRequestDao;
import devops.platform.infrastructure.outbound.database.dao.PullRequestReviewDao;
import devops.platform.infrastructure.outbound.database.utils.Partitioner;

import java.util.List;

@OutboundAdapter
public class PullRequestInventoryDatabaseAdapter implements PullRequestInventory {

    private final PullRequestDao pullRequestDao;
    private final PullRequestReviewDao pullRequestReviewDao;

    public PullRequestInventoryDatabaseAdapter(PullRequestDao pullRequestDao,
                                               PullRequestReviewDao pullRequestReviewDao) {
        this.pullRequestDao = pullRequestDao;
        this.pullRequestReviewDao = pullRequestReviewDao;
    }

    @Override
    public void deleteAllByRepositoryId(Long repositoryId) {
        pullRequestReviewDao.deleteAllByRepositoryId(repositoryId);
        pullRequestDao.deleteAllByRepositoryId(repositoryId);
    }

    @Override
    public void insertAll(Long repositoryId, List<PullRequest> pullRequests) {

        if (pullRequests == null || pullRequests.isEmpty()) {
            return;
        }

        Partitioner
                .partition(pullRequests, 10000)
                .forEach(chunkOfPullRequests -> pullRequestDao.insertAll(repositoryId, chunkOfPullRequests));

        for (PullRequest pr : pullRequests) {
            if (pr.reviews() != null && !pr.reviews().isEmpty()) {
                Long pullRequestId = pullRequestDao.findIdByRepositoryIdAndNumber(repositoryId, pr.number());
                pullRequestReviewDao.insertAll(pullRequestId, pr.reviews());
            }
        }
    }

    @Override
    public List<PullRequest> findAllByRepositoryId(Long repositoryId) {
        return pullRequestDao.findAllByRepositoryId(repositoryId);
    }
}
