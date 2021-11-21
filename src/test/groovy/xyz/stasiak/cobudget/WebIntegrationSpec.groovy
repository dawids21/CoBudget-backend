package xyz.stasiak.cobudget

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@SuppressWarnings('SpringJavaAutowiredMembersInspection')
@ActiveProfiles("test")
abstract class WebIntegrationSpec extends Specification {

    @Autowired
    protected MockMvc mvc

    protected static def getJwt(String userId) {
        SecurityMockMvcRequestPostProcessors.jwt().jwt(token -> token.claim("uid", userId))
    }

}