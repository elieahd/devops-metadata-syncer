package devops.platform.domain.models;

public record Project(Long id,
                      String key,
                      String name) {

    public static Project of(String key, String name) {
        return new Project(null, key, name);
    }
}
