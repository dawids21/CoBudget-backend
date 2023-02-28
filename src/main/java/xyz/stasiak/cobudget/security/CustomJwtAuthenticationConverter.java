package xyz.stasiak.cobudget.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    static final String ROLE_PREFIX = "ROLE_";
    private static final String ROLES_CLAIM = "roles";
    private static final String GROUPS_PREFIX = "CoBudget-";
    private final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

    @Override
    public AbstractAuthenticationToken convert(@SuppressWarnings("NullableProblems") Jwt source) {
        AbstractAuthenticationToken token = jwtAuthenticationConverter.convert(source);
        Collection<GrantedAuthority> authorities = new HashSet<>(token.getAuthorities());
        authorities.addAll(getAuthoritiesFromRoles(source));
        return new JwtAuthenticationToken(source, authorities, token.getName());
    }

    private Collection<GrantedAuthority> getAuthoritiesFromRoles(Jwt source) {
        return source.getClaimAsStringList(ROLES_CLAIM)
                .stream()
                .map(group -> group.replace(GROUPS_PREFIX, ROLE_PREFIX))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
    }
}
