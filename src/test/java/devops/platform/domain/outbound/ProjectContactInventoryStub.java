package devops.platform.domain.outbound;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.ProjectContact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectContactInventoryStub implements ProjectContactInventory {

    private final Map<Long, List<ProjectContact>> contactsPerProjectId;

    public ProjectContactInventoryStub() {
        this.contactsPerProjectId = new HashMap<>();
    }

    @Override
    public void createAll(List<ProjectContact> projectContacts, Project project) {
        contactsPerProjectId
                .computeIfAbsent(project.id(), _ -> new ArrayList<>())
                .addAll(projectContacts);
    }

    @Override
    public List<ProjectContact> findAllByProject(Project project) {
        return contactsPerProjectId.getOrDefault(project.id(), List.of());
    }

}
