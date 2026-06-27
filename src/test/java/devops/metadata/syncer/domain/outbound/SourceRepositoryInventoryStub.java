package devops.metadata.syncer.domain.outbound;

import devops.metadata.syncer.domain.models.Pipeline;
import devops.metadata.syncer.domain.models.PullRequest;
import devops.metadata.syncer.domain.models.Release;
import devops.metadata.syncer.domain.models.Vulnerability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourceRepositoryInventoryStub implements SourceRepositoryInventory {

    private final Map<String, List<PullRequest>> pullRequests;
    private final Map<String, List<Pipeline>> workflows;
    private final Map<String, List<Release>> releases;
    private final Map<String, List<Vulnerability>> vulnerabilities;

    public SourceRepositoryInventoryStub() {
        this.pullRequests = new HashMap<>();
        this.workflows = new HashMap<>();
        this.releases = new HashMap<>();
        this.vulnerabilities = new HashMap<>();
    }

    public void pushPullRequests(String organization, String repository, List<PullRequest> pullRequests) {
        this.pullRequests.put(key(organization, repository), pullRequests);
    }

    public void pushWorkflows(String organization, String repository, List<Pipeline> workflows) {
        this.workflows.put(key(organization, repository), workflows);
    }

    public void pushReleases(String organization, String repository, List<Release> releases) {
        this.releases.put(key(organization, repository), releases);
    }

    public void pushVulnerabilities(String organization, String repository, List<Vulnerability> vulnerabilities) {
        this.vulnerabilities.put(key(organization, repository), vulnerabilities);
    }

    @Override
    public List<PullRequest> findAllPullRequests(String organization, String repository) {
        return pullRequests.getOrDefault(key(organization, repository), List.of());
    }

    @Override
    public List<Pipeline> findAllWorkflows(String organization, String repository) {
        return workflows.getOrDefault(key(organization, repository), List.of());
    }

    @Override
    public List<Release> findAllReleases(String organization, String repository) {
        return releases.getOrDefault(key(organization, repository), List.of());
    }

    @Override
    public List<Vulnerability> findAllVulnerabilities(String organization, String repository) {
        return vulnerabilities.getOrDefault(key(organization, repository), List.of());
    }

    private String key(String organization, String repository) {
        return "%s/%s".formatted(organization, repository);
    }
}