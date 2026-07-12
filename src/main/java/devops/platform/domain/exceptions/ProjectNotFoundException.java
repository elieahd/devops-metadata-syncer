package devops.platform.domain.exceptions;

public class ProjectNotFoundException extends Exception {

    public ProjectNotFoundException(String projectKey) {
        super("Project " + projectKey + " not found");
    }

}
