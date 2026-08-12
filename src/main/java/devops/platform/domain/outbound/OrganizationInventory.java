package devops.platform.domain.outbound;

import devops.platform.domain.models.Organization;

import java.util.Optional;

public interface OrganizationInventory {

    Optional<Organization> findByAcronym(String acronym);

    Organization create(Organization organization);
}
