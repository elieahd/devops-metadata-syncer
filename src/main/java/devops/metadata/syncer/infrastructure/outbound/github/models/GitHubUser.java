package devops.metadata.syncer.infrastructure.outbound.github.models;

public record GitHubUser(String login,
                         String type) {

    public boolean isUser() {
        return "User".equals(type);
    }
}
