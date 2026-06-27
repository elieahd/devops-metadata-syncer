package devops.metadata.syncer.domain.outbound;

import devops.metadata.syncer.domain.models.Release;

import java.util.List;

public interface ReleaseInventory {

    void deleteAllByRepositoryId(Long repositoryId);

    void insertAll(Long repositoryId,
                   List<Release> releases);

    List<Release> findAllByRepositoryId(Long repositoryId);
}
