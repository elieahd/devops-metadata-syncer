package devops.platform.infrastructure.outbound.database;

import devops.platform.domain.models.Pipeline;
import devops.platform.domain.outbound.PipelineInventory;
import devops.platform.infrastructure.outbound.OutboundAdapter;
import devops.platform.infrastructure.outbound.database.dao.PipelineDao;
import devops.platform.infrastructure.outbound.database.dao.PipelineRunDao;
import devops.platform.infrastructure.outbound.database.utils.Partitioner;

import java.util.List;

@OutboundAdapter
public class PipelineInventoryDatabaseAdapter implements PipelineInventory {

    private final PipelineDao pipelineDao;
    private final PipelineRunDao pipelineRunDao;

    public PipelineInventoryDatabaseAdapter(PipelineDao pipelineDao,
                                            PipelineRunDao pipelineRunDao) {
        this.pipelineDao = pipelineDao;
        this.pipelineRunDao = pipelineRunDao;
    }

    @Override
    public void deleteAllByRepositoryId(Long repositoryId) {
        pipelineRunDao.deleteAllByRepositoryId(repositoryId);
        pipelineDao.deleteAllByRepositoryId(repositoryId);
    }

    @Override
    public void insertAll(Long repositoryId, List<Pipeline> pipelines) {

        if (pipelines == null || pipelines.isEmpty()) {
            return;
        }

        pipelineDao.insertAll(repositoryId, pipelines);

        for (Pipeline pipeline : pipelines) {
            if (pipeline.runs() != null && !pipeline.runs().isEmpty()) {
                Long pipelineId = pipelineDao.findIdByRepositoryIdAndSourceId(repositoryId, pipeline.sourceId());
                Partitioner.partition(pipeline.runs(), 10000)
                        .forEach(chunkOfPipelineRuns -> pipelineRunDao.insertAll(pipelineId, chunkOfPipelineRuns));
            }
        }

    }

    @Override
    public List<Pipeline> findAllByRepositoryId(Long repositoryId) {
        return pipelineDao.findAllByRepositoryId(repositoryId);
    }

}
