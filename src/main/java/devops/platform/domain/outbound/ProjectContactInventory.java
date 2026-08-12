package devops.platform.domain.outbound;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.ProjectContact;

import java.util.List;

public interface ProjectContactInventory {

    void createAll(List<ProjectContact> projectContacts,
                   Project project);

    List<ProjectContact> findAllByProject(Project project);
}
