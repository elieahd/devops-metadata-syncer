package devops.platform.infrastructure.outbound.database;

import devops.platform.domain.models.Organization;
import devops.platform.domain.models.assertions.OrganizationAssertions;
import devops.platform.infrastructure.OutboundDatabaseIntegrationTest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.devt.randomizer.RandomizerUtils.random;
import static org.assertj.core.api.Assertions.assertThat;

class OrganizationInventoryDatabaseAdapterTest extends OutboundDatabaseIntegrationTest {

    @Test
    void findByAcronym_shouldReturnOptionalOfOrganization_whenOrganizationExists() {
        // Arrange
        Organization existingOrganization = createOrganization();
        // Act
        Optional<Organization> organization = organizationInventory.findByAcronym(existingOrganization.acronym());
        // Assert
        assertThat(organization).isPresent();
        OrganizationAssertions.assertThat(organization.get()).isEqualTo(existingOrganization);
    }

    @Test
    void findByAcronym_shouldReturnOptionalOfEmpty_whenOrganizationDontExists() {
        // Arrange
        String acronym = random(String.class);
        // Act
        Optional<Organization> organization = organizationInventory.findByAcronym(acronym);
        // Assert
        assertThat(organization).isEmpty();
    }

    @Test
    void create_shouldReturnCreateOrganization() {
        // Arrange
        String acronym = random(String.class);
        String name = random(String.class);
        String description = random(String.class);
        Organization organization = Organization.of(acronym, name, description);
        // Act
        Organization createdOrganization = organizationInventory.create(organization);
        // Assert
        OrganizationAssertions.assertThat(createdOrganization)
                .hasAcronym(acronym)
                .hasName(name)
                .hasDescription(description)
                .hasNonNullId();
    }

}
