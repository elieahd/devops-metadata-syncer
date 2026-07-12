package devops.platform.infrastructure.inbound.rest;

import devops.platform.domain.exceptions.InvalidReportStatusException;
import devops.platform.domain.exceptions.InvalidReportTypeException;
import devops.platform.domain.exceptions.ProjectNotFoundException;
import devops.platform.domain.exceptions.SourceNotFoundException;
import devops.platform.domain.inbound.CreateProjectReport;
import devops.platform.domain.inbound.GetProjects;
import devops.platform.domain.inbound.GetRepositories;
import devops.platform.domain.inbound.SyncProject;
import devops.platform.domain.models.Project;
import devops.platform.domain.models.Repository;
import devops.platform.infrastructure.inbound.rest.requests.CreateReportRequest;
import devops.platform.infrastructure.inbound.rest.responses.ErrorResponse;
import devops.platform.infrastructure.inbound.rest.responses.ProjectView;
import devops.platform.infrastructure.inbound.rest.responses.RepositoryView;
import devops.platform.infrastructure.inbound.rest.responses.ResponseEntityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestControllerDocumented(
        name = "Projects",
        description = "Projects resources",
        path = "/api/v1/projects"
)
public class ProjectRest {

    private final GetProjects getProjects;
    private final GetRepositories getRepositories;
    private final SyncProject syncProject;
    private final CreateProjectReport createProjectReport;

    public ProjectRest(GetProjects getProjects,
                       GetRepositories getRepositories,
                       SyncProject syncProject,
                       CreateProjectReport createProjectReport) {
        this.getProjects = getProjects;
        this.getRepositories = getRepositories;
        this.syncProject = syncProject;
        this.createProjectReport = createProjectReport;
    }

    @GetMapping
    @Operation(
            summary = "Get all projects",
            description = "Retrieve all projects",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Projects",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ProjectView.class)
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<List<ProjectView>> getAllProjects() {
        List<Project> projects = getProjects.getAll();
        return ResponseEntityMapper.ok(projects, ProjectView::map);
    }

    @GetMapping("{projectKey}/repositories")
    @Operation(
            summary = "Get all repositories by project",
            description = "Retrieve all repositories by project",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Repositories by project",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = RepositoryView.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Project not found by key",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ErrorResponse.class,
                                            example = """
                                                    {
                                                        "errorCode": "NOT_FOUND",
                                                        "errorMessage": "Project '{projectKey}' not found"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<List<RepositoryView>> getAllRepositoriesByProjectKey(@PathVariable String projectKey) throws ProjectNotFoundException {
        List<Repository> repositories = getRepositories.getAllByProjectKey(projectKey);
        return ResponseEntityMapper.ok(repositories, RepositoryView::map);
    }

    @PutMapping("{projectKey}/do-sync")
    @Operation(
            summary = "Synchronize all repositories for a given project",
            description = "Synchronize all repositories for a given project",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Project's repositories successfully synchronized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Project not found by key",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ErrorResponse.class,
                                            example = """
                                                    {
                                                        "errorCode": "NOT_FOUND",
                                                        "errorMessage": "Project '{projectKey}' not found"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<Void> syncProject(@PathVariable String projectKey) throws ProjectNotFoundException, SourceNotFoundException {
        syncProject.sync(projectKey);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("{projectKey}/reports/{reportType}")
    @Operation(
            summary = "Add Report to project",
            description = "Add Report to project",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Report added to Project"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Project not found by key",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ErrorResponse.class,
                                            example = """
                                                    {
                                                        "errorCode": "NOT_FOUND",
                                                        "errorMessage": "Project '{projectKey}' not found"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Report Type is not valid",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ErrorResponse.class,
                                            example = """
                                                    {
                                                        "errorCode": "BAD_REQUEST",
                                                        "errorMessage": "'{reportType}' report type is invalid"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Report status is not valid",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ErrorResponse.class,
                                            example = """
                                                    {
                                                        "errorCode": "BAD_REQUEST",
                                                        "errorMessage": "'{reportStatus}' report status is invalid"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<Void> addReportToProject(@PathVariable String projectKey,
                                                   @PathVariable String reportType,
                                                   @RequestBody CreateReportRequest request) throws ProjectNotFoundException, InvalidReportStatusException, InvalidReportTypeException {
        createProjectReport.create(projectKey, reportType, request.status(), request.metadata());
        return ResponseEntity.accepted().build();
    }

}
