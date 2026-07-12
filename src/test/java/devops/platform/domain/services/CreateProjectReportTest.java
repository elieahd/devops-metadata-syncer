package devops.platform.domain.services;

import devops.platform.domain.exceptions.InvalidReportStatusException;
import devops.platform.domain.exceptions.InvalidReportTypeException;
import devops.platform.domain.exceptions.ProjectNotFoundException;
import devops.platform.domain.inbound.CreateProjectReport;
import devops.platform.domain.models.Project;
import devops.platform.domain.models.Report;
import devops.platform.domain.models.ReportStatus;
import devops.platform.domain.models.ReportType;
import devops.platform.domain.models.assertions.ReportAssertions;
import devops.platform.domain.models.randomizers.ProjectRandomizer;
import devops.platform.domain.models.randomizers.ReportRandomizer;
import devops.platform.domain.outbound.ProjectInventory;
import devops.platform.domain.outbound.ProjectInventoryStub;
import devops.platform.domain.outbound.ReportInventory;
import devops.platform.domain.outbound.ReportInventoryStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class CreateProjectReportTest {

    private ProjectInventory projectInventory;
    private ReportInventory reportInventory;
    private CreateProjectReport sut;

    @BeforeEach
    void setup() {
        projectInventory = new ProjectInventoryStub();
        reportInventory = new ReportInventoryStub();
        sut = new ProjectReportService(projectInventory, reportInventory);
    }

    @Test
    void create_shouldThrowException_whenProjectNotFound() {
        // Arrange
        String projectKey = ProjectRandomizer.key();
        String type = random(ReportType.class).toString();
        String status = random(ReportStatus.class).toString();
        String metadata = ReportRandomizer.metadata();
        // Act
        Throwable thrown = catchThrowable(() -> sut.create(projectKey, type, status, metadata));
        // Assert
        assertThat(thrown)
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessage("Project %s not found".formatted(projectKey));
    }

    @Test
    void create_shouldThrowException_whenReportTypeIsNotValid() {
        // Arrange
        Project project = projectInventory.create(Project.of(ProjectRandomizer.key(), random(String.class)));
        String type = random(String.class);
        String status = random(ReportStatus.class).toString();
        String metadata = ReportRandomizer.metadata();
        // Act
        Throwable thrown = catchThrowable(() -> sut.create(project.key(), type, status, metadata));
        // Assert
        assertThat(thrown)
                .isInstanceOf(InvalidReportTypeException.class)
                .hasMessage("'%s' report type is invalid".formatted(type));

    }

    @Test
    void create_shouldThrowException_whenReportStatusIsNotValid() {
        // Arrange
        Project project = projectInventory.create(Project.of(ProjectRandomizer.key(), random(String.class)));
        String type = random(ReportType.class).toString();
        String status = random(String.class);
        String metadata = ReportRandomizer.metadata();
        // Act
        Throwable thrown = catchThrowable(() -> sut.create(project.key(), type, status, metadata));
        // Assert
        assertThat(thrown)
                .isInstanceOf(InvalidReportStatusException.class)
                .hasMessage("'%s' report status is invalid".formatted(status));
    }

    @Test
    void create_shouldStoreProjectReport() throws ProjectNotFoundException, InvalidReportStatusException, InvalidReportTypeException {
        // Arrange
        Project project = projectInventory.create(Project.of(ProjectRandomizer.key(), random(String.class)));
        ReportType type = random(ReportType.class);
        ReportStatus status = random(ReportStatus.class);
        String metadata = ReportRandomizer.metadata();
        // Act
        sut.create(project.key(), type.toString(), status.toString(), metadata);
        // Assert
        List<Report> reports = reportInventory.findAllByProjectId(project.id());
        assertThat(reports)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        ReportAssertions.assertThat(reports.getFirst())
                .hasType(type)
                .hasStatus(status)
                .hasMetadata(metadata)
                .hasCreatedDateAsNow();

    }
}
