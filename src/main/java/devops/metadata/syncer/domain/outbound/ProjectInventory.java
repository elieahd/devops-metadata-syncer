package devops.metadata.syncer.domain.outbound;

import devops.metadata.syncer.domain.models.Project;

import java.util.Optional;

public interface ProjectInventory {

    Optional<Project> findByKey(String key);

    Project create(Project project);

}
