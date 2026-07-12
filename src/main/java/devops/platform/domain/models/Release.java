package devops.platform.domain.models;

import java.time.OffsetDateTime;

public record Release(String name,
                      String tagName,
                      OffsetDateTime publishedAt) {
}
