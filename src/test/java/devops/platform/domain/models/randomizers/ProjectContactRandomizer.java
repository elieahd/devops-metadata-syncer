package devops.platform.domain.models.randomizers;

import com.devt.randomizer.RandomizerUtils;
import devops.platform.domain.models.ProjectContact;

public class ProjectContactRandomizer {

    private final String firstName;
    private final String lastName;
    private final String mail;

    public ProjectContactRandomizer() {
        this.firstName = RandomizerUtils.random(String.class);
        this.lastName = RandomizerUtils.random(String.class);
        this.mail = RandomizerUtils.random(String.class);
    }

    public static ProjectContactRandomizer builder() {
        return new ProjectContactRandomizer();
    }

    public static ProjectContact random() {
        return builder().build();
    }

    public ProjectContact build() {
        return new ProjectContact(firstName, lastName, mail);
    }
}
