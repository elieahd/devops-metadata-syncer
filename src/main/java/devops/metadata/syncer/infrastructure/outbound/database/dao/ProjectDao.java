package devops.metadata.syncer.infrastructure.outbound.database.dao;

import devops.metadata.syncer.domain.models.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProjectDao {

    Project findByKey(@Param("key") String key);

    Long create(@Param("project") Project project);
}
