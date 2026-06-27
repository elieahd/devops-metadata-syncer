package devops.metadata.syncer.infrastructure.outbound.database;

import devops.metadata.syncer.domain.models.PullRequest;
import devops.metadata.syncer.domain.outbound.PullRequestInventory;
import devops.metadata.syncer.infrastructure.outbound.OutboundAdapter;
import devops.metadata.syncer.infrastructure.outbound.database.dao.PullRequestDao;
import devops.metadata.syncer.infrastructure.outbound.database.dao.PullRequestReviewDao;

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

        pullRequestDao.insertAll(repositoryId, pullRequests);

        for (PullRequest pr : pullRequests) {
            if (pr.reviews() != null && !pr.reviews().isEmpty()) {
                pullRequestReviewDao.insertAll(repositoryId, pr.number(), pr.reviews());
            }
        }
    }

    @Override
    public List<PullRequest> findAllByRepositoryId(Long repositoryId) {
        return pullRequestDao.findAllByRepositoryId(repositoryId);
    }
}
