package devops.platform.domain.models.randomizers;

import com.devt.randomizer.RandomizerUtils;
import devops.platform.domain.models.Repository;
import devops.platform.domain.models.RepositorySource;

import java.time.LocalDateTime;

public class RepositoryRandomizer {

    private final Long id;
    private final String organization;
    private final String name;
    private RepositorySource source;
    private LocalDateTime lastSyncTime;

    public RepositoryRandomizer() {
        this.id = RandomizerUtils.random(Long.class);
        this.organization = RandomizerUtils.random(String.class);
        this.name = RandomizerUtils.random(String.class);
        this.source = RandomizerUtils.random(RepositorySource.class);
        this.lastSyncTime = RandomizerUtils.random(LocalDateTime.class);
    }

    public static RepositoryRandomizer builder() {
        return new RepositoryRandomizer();
    }

    public static Repository random() {
        return builder().build();
    }

    public RepositoryRandomizer source(RepositorySource source) {
        this.source = source;
        return this;
    }

    public RepositoryRandomizer lastSyncTime(LocalDateTime lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
        return this;
    }

    public Repository build() {
        return new Repository(
                id,
                organization,
                name,
                source,
                lastSyncTime
        );
    }
}
