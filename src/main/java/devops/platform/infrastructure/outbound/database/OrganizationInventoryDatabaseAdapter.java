package devops.platform.infrastructure.outbound.database;

import devops.platform.domain.models.Organization;
import devops.platform.domain.outbound.OrganizationInventory;
import devops.platform.infrastructure.outbound.OutboundAdapter;
import devops.platform.infrastructure.outbound.database.dao.OrganizationDao;

import java.util.Optional;

@OutboundAdapter
public class OrganizationInventoryDatabaseAdapter implements OrganizationInventory {

    private final OrganizationDao dao;

    public OrganizationInventoryDatabaseAdapter(OrganizationDao dao) {
        this.dao = dao;
    }

    @Override
    public Optional<Organization> findByAcronym(String acronym) {
        Organization organization = dao.findByAcronym(acronym);
        return Optional.ofNullable(organization);
    }

    @Override
    public Organization create(Organization organization) {
        Long id = dao.create(organization);
        return new Organization(
                id,
                organization.acronym(),
                organization.name(),
                organization.description()
        );
    }

}
