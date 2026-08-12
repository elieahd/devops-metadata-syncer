package devops.platform.infrastructure.outbound.database.dao;

import devops.platform.domain.models.Organization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrganizationDao {

    Organization findByAcronym(@Param("acronym") String acronym);

    Long create(@Param("organization") Organization organization);
}
