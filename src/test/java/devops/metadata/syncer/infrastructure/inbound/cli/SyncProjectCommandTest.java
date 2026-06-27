package devops.metadata.syncer.infrastructure.inbound.cli;

import devops.metadata.syncer.domain.inbound.SyncProject;
import devops.metadata.syncer.infrastructure.inbound.cli.exception.MissingArgException;
import devops.metadata.syncer.infrastructure.inbound.cli.helper.ApplicationArgumentsStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class SyncProjectCommandTest {

    private SyncProjectStub syncProject;
    private SyncProjectCommand sut;

    @BeforeEach
    void setUp() {
        syncProject = new SyncProjectStub();
        sut = new SyncProjectCommand(syncProject);
    }

    @Test
    void name_shouldReturnName() {
        // Act
        String commandName = sut.name();
        // Assert
        assertThat(commandName).isEqualTo("sync-project");
    }

    @Test
    void usage_shouldReturnUsage() {
        // Act
        String commandUsage = sut.usage();
        // Assert
        assertThat(commandUsage).isEqualTo("--project=<projectKey>");
    }

    @Test
    void execute_shouldThrowExceptionWhenMissingProjectArg() {
        // Arrange
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of("name", List.of("John"))
        );
        // Act
        Throwable thrown = catchThrowable(() -> sut.execute(args));
        // Assert
        assertThat(thrown)
                .isInstanceOf(MissingArgException.class)
                .hasMessageContaining("Missing required argument: --project");
    }

    @Test
    void execute_shouldSyncProject() throws Exception {
        // Arrange
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of("project", List.of("MyProject"))
        );
        // Act
        sut.execute(args);
        // Assert
        assertThat(syncProject.hasSynced("MyProject")).isTrue();
    }

}

class SyncProjectStub implements SyncProject {

    private final Set<String> projectsSynced;

    public SyncProjectStub() {
        this.projectsSynced = new HashSet<>();
    }

    @Override
    public void sync(String projectKey) {
        projectsSynced.add(projectKey);
    }

    public boolean hasSynced(String projectKey) {
        return projectsSynced.contains(projectKey);
    }
}
