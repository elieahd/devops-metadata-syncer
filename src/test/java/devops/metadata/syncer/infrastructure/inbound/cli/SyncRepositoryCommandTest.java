package devops.metadata.syncer.infrastructure.inbound.cli;

import devops.metadata.syncer.domain.models.RepositorySource;
import devops.metadata.syncer.domain.services.SyncRepositoryStub;
import devops.metadata.syncer.infrastructure.inbound.cli.exception.MissingArgException;
import devops.metadata.syncer.infrastructure.inbound.cli.helper.ApplicationArgumentsStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.ApplicationArguments;

import java.util.List;
import java.util.Map;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class SyncRepositoryCommandTest {

    private SyncRepositoryStub syncRepository;
    private SyncRepositoryCommand sut;

    @BeforeEach
    void setUp() {
        syncRepository = new SyncRepositoryStub();
        sut = new SyncRepositoryCommand(syncRepository);
    }

    @Test
    void name_shouldReturnName() {
        // Act
        String commandName = sut.name();
        // Assert
        assertThat(commandName).isEqualTo("sync-repository");
    }

    @Test
    void usage_shouldReturnUsage() {
        // Act
        String commandUsage = sut.usage();
        // Assert
        assertThat(commandUsage).isEqualTo("--organization=<org> --repository=<repository> --source=<source>");
    }

    @Test
    void execute_shouldThrowExceptionWhenMissingOrganizationArg() {
        // Arrange
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of(
                        "repository", List.of("my-repository"),
                        "source", List.of("GITHUB")
                )
        );
        // Act
        Throwable thrown = catchThrowable(() -> sut.execute(args));
        // Assert
        assertThat(thrown)
                .isInstanceOf(MissingArgException.class)
                .hasMessageContaining("Missing required argument: --organization");
    }

    @Test
    void execute_shouldThrowExceptionWhenMissingRepositoryArg() {
        // Arrange
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of(
                        "organization", List.of("my-org"),
                        "source", List.of("GITHUB")
                )
        );
        // Act
        Throwable thrown = catchThrowable(() -> sut.execute(args));
        // Assert
        assertThat(thrown)
                .isInstanceOf(MissingArgException.class)
                .hasMessageContaining("Missing required argument: --repository");
    }


    @ParameterizedTest
    @EnumSource(RepositorySource.class)
    void execute_shouldSyncProject(RepositorySource source) throws Exception {
        // Arrange
        String organization = random(String.class);
        String repositoryName = random(String.class);
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of(
                        "organization", List.of(organization),
                        "repository", List.of(repositoryName),
                        "source", List.of(source.name())
                )
        );
        // Act
        sut.execute(args);
        // Assert
        assertThat(syncRepository.hasSynced(organization, repositoryName, source)).isTrue();
    }


    @Test
    void execute_shouldSyncProject_whenSourceIsNullItShouldBeOverridenToGithub() throws Exception {
        // Arrange
        String organization = random(String.class);
        String repositoryName = random(String.class);
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of(
                        "organization", List.of(organization),
                        "repository", List.of(repositoryName)
                )
        );
        // Act
        sut.execute(args);
        // Assert
        assertThat(syncRepository.hasSynced(organization, repositoryName, RepositorySource.GITHUB)).isTrue();
    }
}
