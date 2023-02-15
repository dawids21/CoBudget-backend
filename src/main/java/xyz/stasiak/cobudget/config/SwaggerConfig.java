package xyz.stasiak.cobudget.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SwaggerConfig {
    private static final String AUTH_SCHEME_NAME = "auth";
    private static final String AUTH_ENDPOINT = "/v1/authorize";
    private static final String TOKEN_ENDPOINT = "/v1/token";
    private final String issuerUri;

    SwaggerConfig(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
        this.issuerUri = issuerUri;
    }

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(AUTH_SCHEME_NAME, oauth2SecurityScheme()))
                .addSecurityItem(new SecurityRequirement().addList(AUTH_SCHEME_NAME));
    }

    private SecurityScheme oauth2SecurityScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl(issuerUri + AUTH_ENDPOINT)
                                .tokenUrl(issuerUri + TOKEN_ENDPOINT)
                                .scopes(new Scopes().addString("openid", "openid"))));
    }
}
