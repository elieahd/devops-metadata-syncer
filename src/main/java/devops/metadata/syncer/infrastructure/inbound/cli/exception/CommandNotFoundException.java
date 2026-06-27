package devops.metadata.syncer.infrastructure.inbound.cli.exception;

import java.util.Set;

public class CommandNotFoundException extends RuntimeException {

    public CommandNotFoundException(String command,
                                    Set<String> availableCommands) {
        super("Unknown command '%s', available: %s".formatted(command, String.join(",", availableCommands)));
    }

}
