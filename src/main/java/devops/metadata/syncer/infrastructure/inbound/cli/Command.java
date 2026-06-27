package devops.metadata.syncer.infrastructure.inbound.cli;

import org.springframework.boot.ApplicationArguments;

public interface Command {

    String name();

    String usage();

    void execute(ApplicationArguments args) throws Exception;

}