package diplom.blog.model.Enum;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(Permission.USER)),
    MODDERATOR(Set.of(Permission.USER, Permission.MODERATE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorites(){
       return permissions.stream()
               .map(p -> new SimpleGrantedAuthority(p.getAccessPermission()))
               .collect(Collectors.toSet());
    }
}
