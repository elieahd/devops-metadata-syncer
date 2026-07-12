package devops.platform.domain.outbound;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.Report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportInventoryStub implements ReportInventory {

    private final Map<Long, List<Report>> reportsByProjectId;

    public ReportInventoryStub() {
        this.reportsByProjectId = new HashMap<>();
    }

    @Override
    public void create(Report report, Project project) {
        reportsByProjectId
                .computeIfAbsent(project.id(), _ -> new ArrayList<>())
                .add(report);
    }

    @Override
    public List<Report> findAllByProjectId(Long projectId) {
        return reportsByProjectId.getOrDefault(projectId, new ArrayList<>());
    }
}
