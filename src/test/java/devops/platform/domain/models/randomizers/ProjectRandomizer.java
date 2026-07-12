package devops.platform.domain.models.randomizers;

import com.devt.randomizer.RandomizerUtils;
import devops.platform.domain.models.Project;

public class ProjectRandomizer {

    private final Long id;
    private final String key;
    private final String name;

    public ProjectRandomizer() {
        this.id = RandomizerUtils.random(Long.class);
        this.key = key();
        this.name = RandomizerUtils.random(String.class);
    }

    public static ProjectRandomizer builder() {
        return new ProjectRandomizer();
    }

    public static Project random() {
        return builder().build();
    }

    public static String key() {
        return RandomizerUtils
                .random(String.class)
                .trim()
                .replaceAll("[^a-z0-9]", "");
    }

    public Project build() {
        return new Project(id, key, name);
    }

}
