package devops.platform.infrastructure.inbound.rest.responses;

import devops.platform.domain.models.Repository;
import devops.platform.domain.models.RepositorySource;

import java.time.LocalDateTime;

public record RepositoryView(String organization,
                             String name,
                             RepositorySource source,
                             LocalDateTime lastSyncTime) {

    public static RepositoryView map(Repository repository) {
        if (repository == null) {
            return null;
        }
        return new RepositoryView(
                repository.organization(),
                repository.name(),
                repository.source(),
                repository.lastSyncTime()
        );
    }

}
