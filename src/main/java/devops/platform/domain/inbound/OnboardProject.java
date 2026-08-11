package devops.platform.domain.inbound;

import devops.platform.domain.exceptions.OrganizationNotFoundException;
import devops.platform.domain.exceptions.ProjectAlreadyExistsException;
import devops.platform.domain.models.Project;
import devops.platform.domain.models.ProjectContact;

import java.util.List;

public interface OnboardProject {

    Project onboard(String organizationAcronym,
                    String projectKey,
                    String projectName,
                    List<ProjectContact> contacts) throws OrganizationNotFoundException, ProjectAlreadyExistsException;

}
