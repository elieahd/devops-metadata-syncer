package devops.metadata.syncer.infrastructure.outbound.database.dao;

import devops.metadata.syncer.domain.models.Repository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RepositoryDao {

    List<Repository> findAllByProjectId(@Param("projectId") Long projectId);

    void updateLastSyncTime(@Param("repositoryId") Long repositoryId,
                            @Param("lastSyncTime") LocalDateTime lastSyncTime);

    Long create(@Param("projectId") Long projectId,
                @Param("repository") Repository repository);

}
