package devops.metadata.syncer.infrastructure.inbound.cli.exception;

import java.util.Set;

public class MissingArgException extends Exception {

    public MissingArgException(String arg,
                               Set<String> options) {
        super("Missing required argument: --%s=<%s>".formatted(arg, String.join(",", options)));
    }

    public MissingArgException(String arg) {
        super("Missing required argument: --%s".formatted(arg));
    }

}
