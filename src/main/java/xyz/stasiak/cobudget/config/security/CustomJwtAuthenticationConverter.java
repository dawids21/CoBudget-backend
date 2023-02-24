package xyz.stasiak.cobudget.config.security;

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
    private final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    @Override
    public AbstractAuthenticationToken convert(@SuppressWarnings("NullableProblems") Jwt source) {
        AbstractAuthenticationToken token = jwtAuthenticationConverter.convert(source);
        Collection<GrantedAuthority> authorities = new HashSet<>(token.getAuthorities());
        authorities.addAll(getAuthoritiesFromRoles(source));
        return new JwtAuthenticationToken(source, authorities, token.getName());
    }

    private Collection<GrantedAuthority> getAuthoritiesFromRoles(Jwt source) {
        return source.getClaimAsStringList("roles")
                .stream()
                .map(group -> group.replace("CoBudget-", "ROLE_"))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
    }
}
