package devops.platform.infrastructure.outbound.database;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.Report;
import devops.platform.domain.outbound.ReportInventory;
import devops.platform.infrastructure.outbound.OutboundAdapter;
import devops.platform.infrastructure.outbound.database.dao.ReportDao;

import java.util.List;

@OutboundAdapter
public class ReportInventoryDatabaseAdapter implements ReportInventory {

    private final ReportDao dao;

    public ReportInventoryDatabaseAdapter(ReportDao dao) {
        this.dao = dao;
    }

    @Override
    public void create(Report report, Project project) {
        dao.create(report, project);
    }

    @Override
    public List<Report> findAllByProjectId(Long projectId) {
        return dao.findAllByProjectId(projectId);
    }
}
