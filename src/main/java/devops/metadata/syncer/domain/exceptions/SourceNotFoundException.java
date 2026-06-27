package devops.metadata.syncer.domain.exceptions;

public class SourceNotFoundException extends Exception {

    public SourceNotFoundException(String source) {
        super("No synchronizer found for " + source);
    }

}
