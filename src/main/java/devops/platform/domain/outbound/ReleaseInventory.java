package devops.platform.domain.outbound;

import devops.platform.domain.models.Release;

import java.util.List;

public interface ReleaseInventory {

    void deleteAllByRepositoryId(Long repositoryId);

    void insertAll(Long repositoryId,
                   List<Release> releases);

    List<Release> findAllByRepositoryId(Long repositoryId);
}
