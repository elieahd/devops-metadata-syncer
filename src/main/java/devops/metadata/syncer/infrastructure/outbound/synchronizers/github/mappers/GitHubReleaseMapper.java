package devops.metadata.syncer.infrastructure.outbound.synchronizers.github.mappers;

import devops.metadata.syncer.domain.models.Release;
import devops.metadata.syncer.infrastructure.outbound.github.models.GitHubRelease;
import org.springframework.stereotype.Component;

@Component
public class GitHubReleaseMapper implements Mapper {

    public Release map(GitHubRelease release) {
        if (release == null) {
            return null;
        }
        return new Release(
                release.name(),
                release.tagName(),
                release.publishedAt()
        );
    }

}
