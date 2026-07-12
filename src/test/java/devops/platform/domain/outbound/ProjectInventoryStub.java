package devops.platform.domain.outbound;

import devops.platform.domain.models.Project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProjectInventoryStub implements ProjectInventory {

    private final Map<String, Project> projects;

    public ProjectInventoryStub() {
        this.projects = new HashMap<>();
    }

    @Override
    public List<Project> findAll() {
        return projects.values().stream().toList();
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

    @Override
    public boolean existsByKey(String key) {
        return projects.containsKey(key);
    }
}
