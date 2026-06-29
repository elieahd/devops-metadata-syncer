package devops.metadata.syncer.infrastructure.inbound.cli;

import devops.metadata.syncer.domain.inbound.SyncRepository;
import devops.metadata.syncer.domain.models.RepositorySource;
import devops.metadata.syncer.infrastructure.inbound.cli.exception.MissingArgException;
import devops.metadata.syncer.infrastructure.inbound.cli.helper.ApplicationArgumentsHelper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class SyncRepositoryCommand implements Command {

    private final SyncRepository syncRepository;

    public SyncRepositoryCommand(SyncRepository syncRepository) {
        this.syncRepository = syncRepository;
    }

    @Override
    public String name() {
        return "sync-repository";
    }

    @Override
    public String usage() {
        return "--organization=<org> --repository=<repository> --source=<source>";
    }

    @Override
    public void execute(ApplicationArguments args) throws Exception {

        ApplicationArgumentsHelper arguments = ApplicationArgumentsHelper.of(args);

        String organization = arguments
                .getArg("organization")
                .orElseThrow(() -> new MissingArgException("organization"));

        String repositoryName = arguments
                .getArg("repository")
                .orElseThrow(() -> new MissingArgException("repository"));

        RepositorySource source = arguments
                .getArg("source")
                .map(String::toUpperCase)
                .map(RepositorySource::valueOf)
                .orElse(RepositorySource.GITHUB);

        syncRepository.sync(organization, repositoryName, source);
    }

}