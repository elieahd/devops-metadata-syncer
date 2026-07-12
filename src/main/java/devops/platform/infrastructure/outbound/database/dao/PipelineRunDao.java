package devops.platform.infrastructure.outbound.database.dao;

import devops.platform.domain.models.PipelineRun;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PipelineRunDao {

    void insertAll(@Param("pipelineId") Long pipelineId,
                   @Param("runs") List<PipelineRun> runs);

    void deleteAllByRepositoryId(@Param("repositoryId") Long repositoryId);

    List<PipelineRun> findAllByPipelineId(@Param("pipelineId") Long pipelineId);
}
