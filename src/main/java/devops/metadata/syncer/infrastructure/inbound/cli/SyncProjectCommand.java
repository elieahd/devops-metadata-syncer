package devops.metadata.syncer.infrastructure.inbound.cli;

import devops.metadata.syncer.domain.inbound.SyncProject;
import devops.metadata.syncer.infrastructure.inbound.cli.exception.MissingArgException;
import devops.metadata.syncer.infrastructure.inbound.cli.helper.ApplicationArgumentsHelper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class SyncProjectCommand implements Command {

    private final SyncProject syncProject;

    public SyncProjectCommand(SyncProject syncProject) {
        this.syncProject = syncProject;
    }

    @Override
    public String name() {
        return "sync-project";
    }

    @Override
    public String usage() {
        return "--project=<projectKey>";
    }

    @Override
    public void execute(ApplicationArguments args) throws Exception {

        ApplicationArgumentsHelper arguments = ApplicationArgumentsHelper.of(args);

        String projectKey = arguments
                .getArg("project")
                .orElseThrow(() -> new MissingArgException("project"));

        syncProject.sync(projectKey);
    }

}