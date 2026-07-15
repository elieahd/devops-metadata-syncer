package devops.platform.domain.outbound;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.Report;

import java.util.List;

public interface ReportInventory {

    void create(Report report,
                Project project);

    List<Report> findAllByProjectId(Long projectId);

}
