package devops.platform.infrastructure.inbound.rest.responses;

import devops.platform.domain.models.Project;

public record ProjectView(String key,
                          String name) {

    public static ProjectView map(Project project) {
        if (project == null) {
            return null;
        }
        return new ProjectView(project.key(), project.name());
    }

}
