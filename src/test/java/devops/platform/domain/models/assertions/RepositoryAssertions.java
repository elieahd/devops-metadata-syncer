package devops.platform.domain.models.assertions;

import devops.platform.domain.models.Repository;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class RepositoryAssertions extends AbstractAssert<RepositoryAssertions, Repository> {

    public RepositoryAssertions(Repository actual) {
        super(actual, RepositoryAssertions.class);
    }

    public static RepositoryAssertions assertThat(Repository actual) {
        return new RepositoryAssertions(actual);
    }

    public RepositoryAssertions isEqualTo(Repository expected) {

        isNotNull();

        Assertions.assertThat(actual.id()).isEqualTo(expected.id());
        Assertions.assertThat(actual.organization()).isEqualTo(expected.organization());
        Assertions.assertThat(actual.name()).isEqualTo(expected.name());
        Assertions.assertThat(actual.source()).isEqualTo(expected.source());
        Assertions.assertThat(actual.lastSyncTime()).isEqualTo(expected.lastSyncTime());

        return this;
    }

}