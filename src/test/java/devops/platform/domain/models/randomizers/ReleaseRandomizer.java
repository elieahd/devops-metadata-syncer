package devops.platform.domain.models.randomizers;

import com.devt.randomizer.RandomizerUtils;
import devops.platform.domain.models.Release;

import java.time.OffsetDateTime;

public class ReleaseRandomizer {

    private final String name;
    private final String tagName;
    private final OffsetDateTime publishedAt;

    public ReleaseRandomizer() {
        this.name = RandomizerUtils.random(String.class);
        this.tagName = RandomizerUtils.random(String.class);
        this.publishedAt = RandomizerUtils.random(OffsetDateTime.class);
    }

    public static ReleaseRandomizer builder() {
        return new ReleaseRandomizer();
    }

    public static Release random() {
        return builder().build();
    }

    public Release build() {
        return new Release(name, tagName, publishedAt);
    }

}
