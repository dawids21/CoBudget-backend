package xyz.stasiak.cobudget.security;

import org.springframework.security.core.Authentication;

public class SecurityService {
    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> Roles.ADMIN.withPrefix().equals(grantedAuthority.getAuthority()));
    }
}
