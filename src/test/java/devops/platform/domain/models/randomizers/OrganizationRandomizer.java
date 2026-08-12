package devops.platform.domain.models.randomizers;

import com.devt.randomizer.RandomizerUtils;
import devops.platform.domain.models.Organization;

public class OrganizationRandomizer {

    private final Long id;
    private final String acronym;
    private final String name;
    private final String description;

    public OrganizationRandomizer() {
        this.id = RandomizerUtils.random(Long.class);
        this.acronym = RandomizerUtils.random(String.class);
        this.name = RandomizerUtils.random(String.class);
        this.description = RandomizerUtils.random(String.class);
    }

    public static OrganizationRandomizer builder() {
        return new OrganizationRandomizer();
    }

    public static Organization random() {
        return builder().build();
    }

    public Organization build() {
        return new Organization(id, acronym, name, description);
    }
}
