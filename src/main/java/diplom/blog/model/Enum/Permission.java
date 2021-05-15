package diplom.blog.model.Enum;

public enum Permission {
    USER("user:write"),
    MODERATE("user:moderate");

    private final String accessPermission;

    Permission(String accessPermission) {
        this.accessPermission = accessPermission;
    }

    public String getAccessPermission() {
        return accessPermission;
    }
}
