package devops.metadata.syncer.domain.exceptions;

import devops.metadata.syncer.domain.models.RepositorySource;

public class RepositoryNotFoundException extends RuntimeException {

    public RepositoryNotFoundException(String organization,
                                       String repositoryName,
                                       RepositorySource repositorySource) {
        super("Repository %s/%s (%s) not found".formatted(organization, repositoryName, repositorySource));
    }

}
