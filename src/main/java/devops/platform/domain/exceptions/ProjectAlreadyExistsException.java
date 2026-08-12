package devops.platform.domain.exceptions;

public class ProjectAlreadyExistsException extends Exception {

    public ProjectAlreadyExistsException(String projectKey) {
        super("Project '%s' already exists".formatted(projectKey));
    }

}
