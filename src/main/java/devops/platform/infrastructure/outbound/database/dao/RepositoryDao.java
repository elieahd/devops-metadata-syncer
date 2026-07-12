package devops.platform.infrastructure.outbound.database.dao;

import devops.platform.domain.models.Repository;
import devops.platform.domain.models.RepositorySource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RepositoryDao {

    List<Repository> findAllByProjectId(@Param("projectId") Long projectId);

    List<Repository> findAllByProjectKey(@Param("projectKey") String projectKey);

    void updateLastSyncTime(@Param("repositoryId") Long repositoryId,
                            @Param("lastSyncTime") LocalDateTime lastSyncTime);

    Long create(@Param("projectId") Long projectId,
                @Param("repository") Repository repository);

    Repository findOneByOrganizationAndNameAndSource(@Param("organization") String organization,
                                                     @Param("name") String name,
                                                     @Param("source") RepositorySource source);
}
