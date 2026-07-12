package devops.platform.domain.services;

import devops.platform.domain.exceptions.InvalidReportStatusException;
import devops.platform.domain.exceptions.InvalidReportTypeException;
import devops.platform.domain.exceptions.ProjectNotFoundException;
import devops.platform.domain.inbound.CreateProjectReport;
import devops.platform.domain.models.Project;
import devops.platform.domain.models.Report;
import devops.platform.domain.models.ReportStatus;
import devops.platform.domain.models.ReportType;
import devops.platform.domain.outbound.ProjectInventory;
import devops.platform.domain.outbound.ReportInventory;

@DomainService
public class ProjectReportService implements CreateProjectReport {

    private final ProjectInventory projectInventory;
    private final ReportInventory reportInventory;

    public ProjectReportService(ProjectInventory projectInventory,
                                ReportInventory reportInventory) {
        this.projectInventory = projectInventory;
        this.reportInventory = reportInventory;
    }

    @Override
    public void create(String projectKey, String type, String status, String metadata) throws ProjectNotFoundException, InvalidReportTypeException, InvalidReportStatusException {
        Project project = projectInventory.findByKey(projectKey)
                .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        Report report = new Report(parseType(type), parseStatus(status), metadata);

        reportInventory.create(report, project);
    }

    private ReportType parseType(String type) throws InvalidReportTypeException {
        try {
            return ReportType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidReportTypeException(type);
        }
    }

    private ReportStatus parseStatus(String status) throws InvalidReportStatusException {
        try {
            return ReportStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new InvalidReportStatusException(status);
        }
    }

}
