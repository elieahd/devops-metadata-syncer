package devops.platform.infrastructure.outbound.synchronizers.github.mappers;

import devops.platform.domain.models.Release;
import devops.platform.infrastructure.outbound.github.models.GitHubRelease;
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
