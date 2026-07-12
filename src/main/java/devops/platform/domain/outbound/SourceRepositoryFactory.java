package devops.platform.domain.outbound;

import devops.platform.domain.exceptions.SourceNotFoundException;
import devops.platform.domain.models.RepositorySource;

public interface SourceRepositoryFactory {

    SourceRepositoryInventory of(RepositorySource source) throws SourceNotFoundException;

}
