package devops.metadata.syncer.domain.inbound;

import devops.metadata.syncer.domain.exceptions.RepositoryNotFoundException;
import devops.metadata.syncer.domain.exceptions.SourceNotFoundException;
import devops.metadata.syncer.domain.models.Repository;
import devops.metadata.syncer.domain.models.RepositorySource;

public interface SyncRepository {

    void sync(String organization,
              String repositoryName,
              RepositorySource repositorySource) throws RepositoryNotFoundException, SourceNotFoundException;

    void sync(Repository repository) throws SourceNotFoundException;
}

