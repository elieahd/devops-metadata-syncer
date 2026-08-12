package devops.platform.domain.models.assertions;

import devops.platform.domain.models.Organization;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class OrganizationAssertions extends AbstractAssert<OrganizationAssertions, Organization> {

    public OrganizationAssertions(Organization actual) {
        super(actual, OrganizationAssertions.class);
    }

    public static OrganizationAssertions assertThat(Organization actual) {
        return new OrganizationAssertions(actual);
    }

    public OrganizationAssertions isEqualTo(Organization expected) {

        isNotNull();

        Assertions.assertThat(actual.id()).isEqualTo(expected.id());
        hasAcronym(expected.acronym());
        hasName(expected.name());
        hasDescription(expected.description());

        return this;
    }

    public OrganizationAssertions hasAcronym(String acronym) {
        Assertions.assertThat(actual.acronym()).isEqualTo(acronym);
        return this;
    }

    public OrganizationAssertions hasName(String name) {
        Assertions.assertThat(actual.name()).isEqualTo(name);
        return this;
    }

    public OrganizationAssertions hasDescription(String description) {
        Assertions.assertThat(actual.description()).isEqualTo(description);
        return this;
    }

    public OrganizationAssertions hasNonNullId() {
        Assertions.assertThat(actual.id()).isNotNull();
        return this;
    }
}

