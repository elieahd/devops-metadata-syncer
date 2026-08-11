package devops.platform.infrastructure.inbound.rest.requests;

import devops.platform.domain.models.ProjectContact;

import java.util.List;

public record OnboardProjectRequest(String organizationAcronym,
                                    String projectKey,
                                    String projectName,
                                    List<ProjectContact> contacts) {
}
