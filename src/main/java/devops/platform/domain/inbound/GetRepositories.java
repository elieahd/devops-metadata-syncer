package devops.platform.domain.inbound;

import devops.platform.domain.exceptions.ProjectNotFoundException;
import devops.platform.domain.models.Repository;

import java.util.List;

public interface GetRepositories {

    List<Repository> getAllByProjectKey(String projectKey) throws ProjectNotFoundException;

}
