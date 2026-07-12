package devops.platform.domain.outbound;

import devops.platform.domain.models.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectInventory {

    List<Project> findAll();

    Optional<Project> findByKey(String key);

    Project create(Project project);

    boolean existsByKey(String projectKey);
}
