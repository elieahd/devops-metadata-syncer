package devops.platform.infrastructure.outbound.database.dao;

import devops.platform.domain.models.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectDao {

    List<Project> findAll();

    Project findByKey(@Param("key") String key);

    Long create(@Param("project") Project project);

    boolean existsByKey(@Param("key") String key);
}
