package com.deltasmarttech.companyorganization.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum AppRole {
    MANAGER(
            Set.of(
                    Permission.MANAGER_CREATE,
                    Permission.MANAGER_READ,
                    Permission.MANAGER_DELETE,
                    Permission.MANAGER_UPDATE
            )
    ),
    ADMIN(
            Set.of(
                    Permission.ADMIN_CREATE,
                    Permission.ADMIN_READ,
                    Permission.ADMIN_DELETE,
                    Permission.ADMIN_UPDATE,
                    Permission.MANAGER_CREATE,
                    Permission.MANAGER_READ,
                    Permission.MANAGER_DELETE,
                    Permission.MANAGER_UPDATE
            )
    ),
    EMPLOYEE(
        Set.of(
            Permission.EMPLOYEE_READ
        )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
