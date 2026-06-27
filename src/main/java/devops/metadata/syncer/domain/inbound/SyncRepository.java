package devops.metadata.syncer.domain.inbound;

import devops.metadata.syncer.domain.exceptions.SourceNotFoundException;
import devops.metadata.syncer.domain.models.Repository;

public interface SyncRepository {

    void sync(Repository repository) throws SourceNotFoundException;
}

