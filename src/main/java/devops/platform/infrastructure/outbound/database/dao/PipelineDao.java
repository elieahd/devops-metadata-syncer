package devops.platform.infrastructure.outbound.database.dao;

import devops.platform.domain.models.Pipeline;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PipelineDao {

    void insertAll(@Param("repositoryId") Long repositoryId,
                   @Param("pipelines") List<Pipeline> pipelines);

    void deleteAllByRepositoryId(@Param("repositoryId") Long repositoryId);

    List<Pipeline> findAllByRepositoryId(@Param("repositoryId") Long repositoryId);

    Long findIdByRepositoryIdAndSourceId(@Param("repositoryId") Long repositoryId,
                                         @Param("pipelineSourceId") String pipelineSourceId);
}
