package devops.platform.domain.outbound;

import devops.platform.domain.models.Organization;
import devops.platform.domain.models.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectInventory {

    List<Project> findAll();

    Optional<Project> findByKey(String key);

    Project create(Project project, Organization organization);

    boolean existsByKey(String projectKey);
}
