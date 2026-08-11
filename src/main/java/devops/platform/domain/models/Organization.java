package devops.platform.domain.models;

public record Organization(Long id,
                           String acronym,
                           String name,
                           String description) {

    public static Organization of(String acronym, String name, String description) {
        return new Organization(null, acronym, name, description);
    }
}
