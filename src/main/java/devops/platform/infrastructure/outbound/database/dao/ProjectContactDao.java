package devops.platform.infrastructure.outbound.database.dao;

import devops.platform.domain.models.ProjectContact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectContactDao {

    void insertAll(@Param("contacts") List<ProjectContact> contacts,
                   @Param("projectId") Long projectId);

    List<ProjectContact> findAllByProjectId(@Param("projectId") Long projectId);
}
