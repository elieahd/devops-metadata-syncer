package devops.platform.domain.inbound;

import devops.platform.domain.exceptions.RepositoryNotFoundException;
import devops.platform.domain.exceptions.SourceNotFoundException;
import devops.platform.domain.models.Repository;
import devops.platform.domain.models.RepositorySource;

public interface SyncRepository {

    void sync(String organization,
              String repositoryName,
              RepositorySource repositorySource) throws RepositoryNotFoundException, SourceNotFoundException;

    void sync(Repository repository) throws SourceNotFoundException;
}

