package xyz.stasiak.cobudget.category

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.mock.DetachedMockFactory

@ActiveProfiles("test")
abstract class WebIntegrationSpec extends Specification {

    @Autowired
    protected MockMvc mvc

    protected static def getJwt(String userId) {
        SecurityMockMvcRequestPostProcessors.jwt().jwt(token -> token.claim("uid", userId))
    }

    @TestConfiguration
    static class WebIntegrationMockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        JwtDecoder jwtDecoder() {
            detachedMockFactory.Mock(JwtDecoder)
        }
    }
}