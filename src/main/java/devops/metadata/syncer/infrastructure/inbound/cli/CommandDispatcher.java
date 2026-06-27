package devops.metadata.syncer.infrastructure.inbound.cli;

import devops.metadata.syncer.infrastructure.inbound.cli.exception.CommandNotFoundException;
import devops.metadata.syncer.infrastructure.inbound.cli.exception.MissingArgException;
import devops.metadata.syncer.infrastructure.inbound.cli.helper.ApplicationArgumentsHelper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Profile("!outbound-test")
public class CommandDispatcher implements ApplicationRunner {

    private final Map<String, Command> commands;

    public CommandDispatcher(List<Command> commands) {
        this.commands = commands
                .stream()
                .collect(Collectors.toMap(Command::name, Function.identity()));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String commandName = ApplicationArgumentsHelper.of(args)
                .getArg("command")
                .orElseThrow(() -> new MissingArgException("command", commands.keySet()));

        Command command = commands.get(commandName);

        if (command == null) {
            throw new CommandNotFoundException(commandName, commands.keySet());
        }

        command.execute(args);
    }
}
