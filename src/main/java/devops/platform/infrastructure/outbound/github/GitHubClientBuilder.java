package devops.platform.infrastructure.outbound.github;

import java.util.Objects;

public class GitHubClientBuilder {

    private String url;
    private String token;

    private GitHubClientBuilder() {
        this.url = "https://api.github.com";
    }

    public static GitHubClientBuilder builder() {
        return new GitHubClientBuilder();
    }

    public GitHubClientBuilder token(String token) {
        this.token = token;
        return this;
    }

    public GitHubClientBuilder url(String url) {
        this.url = url;
        return this;
    }

    public GitHubClient build() {
        Objects.requireNonNull(token, "GitHub Client 'token' must not be null");
        Objects.requireNonNull(url, "GitHub Client 'url' must not be null");
        return new GitHubClientAdapter(url, token);
    }
}
