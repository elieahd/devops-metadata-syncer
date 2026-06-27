package devops.metadata.syncer.infrastructure.inbound.cli;

import devops.metadata.syncer.infrastructure.inbound.cli.exception.CommandNotFoundException;
import devops.metadata.syncer.infrastructure.inbound.cli.exception.MissingArgException;
import devops.metadata.syncer.infrastructure.inbound.cli.helper.ApplicationArgumentsStub;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class CommandDispatcherTest {

    @Test
    void run_executesMatchingCommand() throws Exception {
        // Arrange
        FakeCommand importCommand = new FakeCommand("import");
        FakeCommand exportCommand = new FakeCommand("export");

        CommandDispatcher sut = new CommandDispatcher(List.of(importCommand, exportCommand));
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of("command", List.of("import"))
        );
        // Act
        sut.run(args);
        // Assert
        assertThat(importCommand.wasExecuted()).isTrue();
        assertThat(importCommand.getReceivedArgs()).isSameAs(args);
        assertThat(exportCommand.wasExecuted()).isFalse();
    }

    @Test
    void run_throwsMissingArgException_whenCommandArgIsAbsent() {
        // Arrange
        FakeCommand importCommand = new FakeCommand("import");
        CommandDispatcher sut = new CommandDispatcher(List.of(importCommand));
        ApplicationArguments args = new ApplicationArgumentsStub(Map.of());
        // Act
        Throwable thrown = catchThrowable(() -> sut.run(args));
        // Assert
        assertThat(thrown).isInstanceOf(MissingArgException.class);
        assertThat(importCommand.wasExecuted()).isFalse();
    }

    @Test
    void run_throwsCommandNotFoundException_whenCommandNameIsUnknown() {
        // Arrange
        FakeCommand importCommand = new FakeCommand("import");
        CommandDispatcher sut = new CommandDispatcher(List.of(importCommand));
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of("command", List.of("doesNotExist"))
        );
        // Act
        Throwable thrown = catchThrowable(() -> sut.run(args));
        // Assert
        assertThat(thrown).isInstanceOf(CommandNotFoundException.class);
        assertThat(importCommand.wasExecuted()).isFalse();
    }

    @Test
    void constructor_buildsLookupKeyedByCommandName() throws Exception {
        // Arrange
        FakeCommand a = new FakeCommand("a");
        FakeCommand b = new FakeCommand("b");
        CommandDispatcher sut = new CommandDispatcher(List.of(a, b));
        ApplicationArguments args = new ApplicationArgumentsStub(
                Map.of("command", List.of("b"))
        );
        // Act
        sut.run(args);
        // Assert
        assertThat(b.wasExecuted()).isTrue();
        assertThat(a.wasExecuted()).isFalse();
    }
}