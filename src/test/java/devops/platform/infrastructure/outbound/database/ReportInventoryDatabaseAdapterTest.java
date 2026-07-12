package devops.platform.infrastructure.outbound.database;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.Report;
import devops.platform.domain.models.assertions.ReportAssertions;
import devops.platform.domain.models.randomizers.ReportRandomizer;
import devops.platform.domain.outbound.ProjectInventory;
import devops.platform.infrastructure.OutboundDatabaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;

class ReportInventoryDatabaseAdapterTest extends OutboundDatabaseIntegrationTest {

    @Autowired
    private ReportInventoryDatabaseAdapter sut;

    @Autowired
    private ProjectInventory projectInventory;

    @Test
    void findAllByProjectId_shouldReturnReportsByProjectId() {
        // Arrange
        Project project = createProject();
        List<Report> projectReports = List.of(
                createReport(project),
                createReport(project)
        );

        Project anotherProject = createProject();
        createReport(anotherProject);
        // Act
        List<Report> reports = sut.findAllByProjectId(project.id());
        // Assert
        assertThat(reports)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        for (int i = 0; i < reports.size(); i++) {
            Report report = reports.get(i);
            Report expectedReport = projectReports.get(i);
            ReportAssertions.assertThat(report).isEqualTo(expectedReport);
        }
    }

    @Test
    void findAllByProjectId_shouldReturnEmptyList_whenNoReportFound() {
        // Arrange
        Project project = createProject();
        // Act
        List<Report> reports = sut.findAllByProjectId(project.id());
        // Assert
        assertThat(reports)
                .isNotNull()
                .isEmpty();
    }

    private Report createReport(Project project) {
        Report report = ReportRandomizer.random();
        sut.create(report, project);
        return report;
    }

    private Project createProject() {
        Project project = Project.of(random(String.class), random(String.class));
        return projectInventory.create(project);
    }

}
