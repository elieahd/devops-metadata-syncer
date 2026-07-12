package devops.platform.domain.inbound;

import devops.platform.domain.exceptions.ProjectNotFoundException;
import devops.platform.domain.exceptions.SourceNotFoundException;

public interface SyncProject {

    void sync(String projectKey) throws ProjectNotFoundException, SourceNotFoundException;

}
