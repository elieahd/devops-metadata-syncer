package devops.metadata.syncer.infrastructure.outbound.database.dao;

import devops.metadata.syncer.domain.models.PipelineRun;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PipelineRunDao {

    void insertAll(@Param("repositoryId") Long repositoryId,
                   @Param("pipelineSourceId") String pipelineSourceId,
                   @Param("runs") List<PipelineRun> runs);

    void deleteAllByRepositoryId(@Param("repositoryId") Long repositoryId);

    List<PipelineRun> findAllByPipelineId(@Param("pipelineId") Long pipelineId);
}
