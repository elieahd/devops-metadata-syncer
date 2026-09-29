package devops.platform.domain.outbound;

import devops.platform.domain.models.Organization;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OrganizationInventoryStub implements OrganizationInventory {

    private final Map<String, Organization> organizationsByAcronym;

    public OrganizationInventoryStub() {
        this.organizationsByAcronym = new HashMap<>();
    }

    @Override
    public Optional<Organization> findByAcronym(String acronym) {
        return Optional.ofNullable(organizationsByAcronym.get(acronym));
    }

    @Override
    public Organization create(Organization organization) {
        organizationsByAcronym.put(organization.acronym(), organization);
        return organization;
    }
}
