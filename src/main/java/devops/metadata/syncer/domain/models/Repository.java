package devops.metadata.syncer.domain.models;

import java.time.LocalDateTime;

public record Repository(Long id,
                         String organization,
                         String name,
                         RepositorySource source,
                         LocalDateTime lastSyncTime) {

    public static Repository of(String organization,
                                String name,
                                RepositorySource source) {
        return new Repository(null, organization, name, source, null);
    }

}
