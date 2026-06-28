package devops.metadata.syncer.domain.models.randomizers;

import com.devt.randomizer.RandomizerUtils;
import devops.metadata.syncer.domain.models.Project;

public class ProjectRandomizer {

    private final Long id;
    private final String key;
    private final String name;

    public ProjectRandomizer() {
        this.id = RandomizerUtils.random(Long.class);
        this.key = RandomizerUtils.random(String.class);
        this.name = RandomizerUtils.random(String.class);
    }

    public static ProjectRandomizer builder() {
        return new ProjectRandomizer();
    }

    public static Project random() {
        return builder().build();
    }

    public Project build() {
        return new Project(id, key, name);
    }

}
