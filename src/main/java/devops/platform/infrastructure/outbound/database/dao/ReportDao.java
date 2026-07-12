package devops.platform.infrastructure.outbound.database.dao;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportDao {

    void create(@Param("report") Report report,
                @Param("project") Project project);

    List<Report> findAllByProjectId(@Param("projectId") Long projectId);

}
