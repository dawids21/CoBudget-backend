package xyz.stasiak.cobudget.common;

import io.vavr.control.Option;
import org.springframework.security.oauth2.jwt.Jwt;

public record UserId(String id) {

    public static Option<UserId> get(Jwt jwt) {
        return Option.of(jwt.getClaim("uid"));
    }

}
