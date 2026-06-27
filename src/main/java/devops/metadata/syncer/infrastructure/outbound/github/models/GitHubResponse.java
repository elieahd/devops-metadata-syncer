package devops.metadata.syncer.infrastructure.outbound.github.models;

public record GitHubResponse<T>(T data, String nextUrl) {
}