package devops.metadata.syncer.domain.outbound;

import devops.metadata.syncer.domain.models.PullRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PullRequestInventoryStub implements PullRequestInventory {

    private final Map<Long, List<PullRequest>> pullRequestsByRepositoryId;

    public PullRequestInventoryStub() {
        this.pullRequestsByRepositoryId = new HashMap<>();
    }

    @Override
    public void deleteAllByRepositoryId(Long repositoryId) {
        pullRequestsByRepositoryId.remove(repositoryId);
    }

    @Override
    public void insertAll(Long repositoryId, List<PullRequest> pullRequests) {
        pullRequestsByRepositoryId
                .computeIfAbsent(repositoryId, id -> new ArrayList<>())
                .addAll(pullRequests);
    }

    @Override
    public List<PullRequest> findAllByRepositoryId(Long repositoryId) {
        return pullRequestsByRepositoryId.getOrDefault(repositoryId, List.of());
    }
}