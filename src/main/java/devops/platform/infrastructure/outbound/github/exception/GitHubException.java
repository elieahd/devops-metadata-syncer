package devops.platform.infrastructure.outbound.github.exception;

import java.io.IOException;

public class GitHubException extends RuntimeException {

    public GitHubException(int code, String message) {
        super("GitHub API Exception : %d - %s".formatted(code, message));
    }

    public GitHubException(IOException e) {
        super(e);
    }

}
