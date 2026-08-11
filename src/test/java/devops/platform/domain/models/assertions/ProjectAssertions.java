package devops.platform.domain.models.assertions;

import devops.platform.domain.models.Project;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ProjectAssertions extends AbstractAssert<ProjectAssertions, Project> {

    public ProjectAssertions(Project actual) {
        super(actual, ProjectAssertions.class);
    }

    public static ProjectAssertions assertThat(Project actual) {
        return new ProjectAssertions(actual);
    }

    public ProjectAssertions isEqualTo(Project expected) {

        isNotNull();

        Assertions.assertThat(actual.id()).isEqualTo(expected.id());
        hasKey(expected.key());
        hasName(expected.name());

        return this;
    }

    public ProjectAssertions hasKey(String key) {
        Assertions.assertThat(actual.key()).isEqualTo(key);
        return this;
    }

    public ProjectAssertions hasName(String name) {
        Assertions.assertThat(actual.name()).isEqualTo(name);
        return this;
    }

    public ProjectAssertions hasNonNullId() {
        Assertions.assertThat(actual.id()).isNotNull();
        return this;
    }
}