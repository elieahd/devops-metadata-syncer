package devops.metadata.syncer.domain.inbound;

import devops.metadata.syncer.domain.exceptions.ProjectNotFoundException;
import devops.metadata.syncer.domain.exceptions.SourceNotFoundException;

public interface SyncProject {

    void sync(String projectKey) throws ProjectNotFoundException, SourceNotFoundException;

}
