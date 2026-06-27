package devops.metadata.syncer.domain.outbound;

import devops.metadata.syncer.domain.exceptions.SourceNotFoundException;
import devops.metadata.syncer.domain.models.RepositorySource;

public interface SourceRepositoryFactory {

    SourceRepositoryInventory of(RepositorySource source) throws SourceNotFoundException;

}
