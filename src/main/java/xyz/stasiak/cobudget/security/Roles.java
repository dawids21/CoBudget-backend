package xyz.stasiak.cobudget.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
enum Roles {
    USER("user"), ADMIN("admin");
    private final String name;

    public String withPrefix() {
        return CustomJwtAuthenticationConverter.ROLE_PREFIX + name;
    }
}
