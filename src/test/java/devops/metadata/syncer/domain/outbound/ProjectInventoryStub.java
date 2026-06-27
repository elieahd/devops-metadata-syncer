package devops.metadata.syncer.domain.outbound;

import devops.metadata.syncer.domain.models.Project;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProjectInventoryStub implements ProjectInventory {

    private final Map<String, Project> projects;

    public ProjectInventoryStub() {
        this.projects = new HashMap<>();
    }

    @Override
    public Optional<Project> findByKey(String key) {
        if (projects.containsKey(key)) {
            return Optional.of(projects.get(key));
        }
        return Optional.empty();
    }

    @Override
    public Project create(Project project) {
        projects.put(project.key(), project);
        return project;
    }
}
