package devops.metadata.syncer.domain.models;

import java.time.OffsetDateTime;

public record Release(String name,
                      String tagName,
                      OffsetDateTime publishedAt) {
}
