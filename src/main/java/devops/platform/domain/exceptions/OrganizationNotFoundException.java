package devops.platform.domain.exceptions;

public class OrganizationNotFoundException extends Exception {

    public OrganizationNotFoundException(String organizationAcronym) {
        super("Organization '%s' not found".formatted(organizationAcronym));
    }

}
