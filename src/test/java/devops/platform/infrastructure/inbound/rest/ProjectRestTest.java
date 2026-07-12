package devops.platform.infrastructure.inbound.rest;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.Report;
import devops.platform.domain.models.ReportStatus;
import devops.platform.domain.models.ReportType;
import devops.platform.domain.models.Repository;
import devops.platform.domain.models.RepositorySource;
import devops.platform.domain.models.assertions.ReportAssertions;
import devops.platform.domain.models.randomizers.ProjectRandomizer;
import devops.platform.domain.models.randomizers.ReportRandomizer;
import devops.platform.domain.outbound.ProjectInventory;
import devops.platform.domain.outbound.ReportInventory;
import devops.platform.domain.outbound.RepositoryInventory;
import devops.platform.infrastructure.RestIntegrationTest;
import devops.platform.infrastructure.inbound.rest.requests.CreateReportRequest;
import devops.platform.infrastructure.inbound.rest.responses.ErrorResponse;
import devops.platform.infrastructure.inbound.rest.responses.ErrorResponseAssertions;
import devops.platform.infrastructure.inbound.rest.responses.ProjectView;
import devops.platform.infrastructure.inbound.rest.responses.RepositoryView;
import devops.platform.infrastructure.inbound.rest.responses.ResponseEntityAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;

class ProjectRestTest extends RestIntegrationTest {

    @Autowired
    private ProjectInventory projectInventory;

    @Autowired
    private RepositoryInventory repositoryInventory;

    @Autowired
    private ReportInventory reportInventory;

    @Test
    @Sql(scripts = {"/sql/clean-up.sql"})
    void getAllProjects_shouldReturnEmptyList_whenNoProjectsFound() {
        // Act
        ResponseEntity<String> response = get("api/v1/projects", String.class);
        // Assert
        ResponseEntityAssertions.assertThat(response).is204();
        assertThat(response.getBody()).isNull();
    }

    @Test
    @Sql(scripts = {"/sql/clean-up.sql"})
    void getAllProjects_shouldReturnAllProjects() {
        // Arrange
        Project project1 = createProject();
        Project project2 = createProject();
        Project project3 = createProject();
        List<Project> allProjects = List.of(project1, project2, project3);
        // Act
        ResponseEntity<List<ProjectView>> response = get("api/v1/projects", new ParameterizedTypeReference<>() {
        });
        // Assert
        ResponseEntityAssertions.assertThat(response).is200();
        assertThat(response.getBody())
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);

        for (int i = 0; i < response.getBody().size(); i++) {
            ProjectView projectView = response.getBody().get(i);
            Project project = allProjects.get(i);
            assertThat(projectView.key()).isEqualTo(project.key());
            assertThat(projectView.name()).isEqualTo(project.name());
        }
    }

    @Test
    void getAllRepositoriesByProjectKey_shouldReturnEmptyList_whenNoRepositoriesFoundForAGivenProject() {
        // Arrange
        Project project = createProject();
        // Act
        ResponseEntity<String> response = get("api/v1/projects/%s/repositories".formatted(project.key()), String.class);
        // Assert
        ResponseEntityAssertions.assertThat(response).is204();
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getAllRepositoriesByProjectKey_shouldThrowException_whenProjectNotFound() {
        // Arrange
        String projectKey = ProjectRandomizer.key();
        // Act
        ResponseEntity<ErrorResponse> response = get("api/v1/projects/%s/repositories".formatted(projectKey), ErrorResponse.class);
        // Assert
        ResponseEntityAssertions.assertThat(response).is404();
        ErrorResponseAssertions.assertThat(response.getBody())
                .hasCode("NOT_FOUND")
                .hasMessage("Project %s not found".formatted(projectKey));
    }

    @Test
    void getAllRepositoriesByProjectKey_shouldReturnAllRepositoriesForAProject() {
        // Arrange
        Project project = createProject();
        List<Repository> allProjectRepositories = List.of(
                createRepository(project),
                createRepository(project),
                createRepository(project)
        );

        Project anotherProject = createProject();
        createRepository(anotherProject);
        // Act
        ResponseEntity<List<RepositoryView>> response = get("api/v1/projects/%s/repositories".formatted(project.key()), new ParameterizedTypeReference<>() {
        });
        // Assert
        ResponseEntityAssertions.assertThat(response).is200();
        assertThat(response.getBody())
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);

        for (int i = 0; i < response.getBody().size(); i++) {
            RepositoryView repositoryView = response.getBody().get(i);
            Repository repository = allProjectRepositories.get(i);
            assertThat(repositoryView.organization()).isEqualTo(repository.organization());
            assertThat(repositoryView.name()).isEqualTo(repository.name());
            assertThat(repositoryView.source()).isEqualTo(repository.source());
            assertThat(repositoryView.lastSyncTime()).isEqualTo(repository.lastSyncTime());
        }
    }

    @Test
    void syncProject_shouldThrowException_whenProjectNotFound() {
        // Arrange
        String projectKey = ProjectRandomizer.key();
        // Act
        ResponseEntity<ErrorResponse> response = put("api/v1/projects/%s/do-sync".formatted(projectKey), ErrorResponse.class);
        // Assert
        ResponseEntityAssertions.assertThat(response).is404();
        ErrorResponseAssertions.assertThat(response.getBody())
                .hasCode("NOT_FOUND")
                .hasMessage("Project %s not found".formatted(projectKey));
    }

    @Test
    void addReportToProject_shouldThrowException_whenProjectNotFound() {
        // Arrange
        String projectKey = ProjectRandomizer.key();
        String reportType = random(ReportType.class).toString();
        CreateReportRequest request = new CreateReportRequest(
                random(ReportStatus.class).toString(),
                ReportRandomizer.metadata()
        );
        // Act
        ResponseEntity<ErrorResponse> response = post("api/v1/projects/%s/reports/%s".formatted(projectKey, reportType), request, ErrorResponse.class);
        // Assert
        ResponseEntityAssertions.assertThat(response).is404();
        ErrorResponseAssertions.assertThat(response.getBody())
                .hasCode("NOT_FOUND")
                .hasMessage("Project %s not found".formatted(projectKey));
    }

    @Test
    void addReportToProject_shouldThrowException_whenReportTypeIsInvalid() {
        // Arrange
        Project project = createProject();
        String reportType = random(String.class).replaceAll("[^a-z0-9]", "");
        CreateReportRequest request = new CreateReportRequest(
                random(ReportStatus.class).toString(),
                ReportRandomizer.metadata()
        );
        // Act
        ResponseEntity<ErrorResponse> response = post("api/v1/projects/%s/reports/%s".formatted(project.key(), reportType), request, ErrorResponse.class);
        // Assert
        ResponseEntityAssertions.assertThat(response).is400();
        ErrorResponseAssertions.assertThat(response.getBody())
                .hasCode("BAD_REQUEST")
                .hasMessage("'%s' report type is invalid, available options: [MORNING_CHECK]".formatted(reportType));
    }

    @Test
    void addReportToProject_shouldThrowException_whenReportStatusIsInvalid() {
        // Arrange
        Project project = createProject();
        String reportType = random(ReportType.class).toString();
        CreateReportRequest request = new CreateReportRequest(
                random(String.class),
                ReportRandomizer.metadata()
        );
        // Act
        ResponseEntity<ErrorResponse> response = post("api/v1/projects/%s/reports/%s".formatted(project.key(), reportType), request, ErrorResponse.class);
        // Assert
        ResponseEntityAssertions.assertThat(response).is400();
        ErrorResponseAssertions.assertThat(response.getBody())
                .hasCode("BAD_REQUEST")
                .hasMessage("'%s' report status is invalid, available options: [SUCCESS, FAILED]".formatted(request.status()));
    }

    @Test
    void addReportToProject_shouldStoreReport() {
        // Arrange
        Project project = createProject();
        String reportType = random(ReportType.class).toString();
        CreateReportRequest request = new CreateReportRequest(
                random(ReportStatus.class).toString(),
                ReportRandomizer.metadata()
        );
        // Act
        ResponseEntity<String> response = post("api/v1/projects/%s/reports/%s".formatted(project.key(), reportType), request, String.class);
        // Assert
        ResponseEntityAssertions.assertThat(response).is202();
        List<Report> reports = reportInventory.findAllByProjectId(project.id());
        assertThat(reports)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        ReportAssertions.assertThat(reports.getFirst())
                .hasType(ReportType.valueOf(reportType))
                .hasStatus(ReportStatus.valueOf(request.status()))
                .hasMetadata(request.metadata())
                .hasCreatedDateAsNow();
    }

    private Project createProject() {
        Project project = Project.of(ProjectRandomizer.key(), random(String.class));
        return projectInventory.create(project);
    }

    private Repository createRepository(Project project) {
        return repositoryInventory.create(project.id(), Repository.of(random(String.class), random(String.class), random(RepositorySource.class)));
    }

}

// TODO syncProject_shouldSyncAllRepositories
// TODO handle metadata projects report
