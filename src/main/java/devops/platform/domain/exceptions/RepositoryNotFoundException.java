package devops.platform.domain.exceptions;

import devops.platform.domain.models.RepositorySource;

public class RepositoryNotFoundException extends RuntimeException {

    public RepositoryNotFoundException(String organization,
                                       String repositoryName,
                                       RepositorySource repositorySource) {
        super("Repository %s/%s (%s) not found".formatted(organization, repositoryName, repositorySource));
    }

}
