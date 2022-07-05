package xyz.stasiak.cobudget.plan

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.mock.DetachedMockFactory
import xyz.stasiak.cobudget.WebIntegrationSpec
import xyz.stasiak.cobudget.common.UserId

import java.time.LocalDate
import java.time.Month

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest(PlanController)
@Import(WebIntegrationMockConfig)
class PlanControllerIntegrationSpec extends WebIntegrationSpec {

    @Autowired
    PlanApplicationService planApplicationService

    private static final def USER_ID = new UserId("userId")

    def "create new plans"() {

        when:
        def response = mvc.perform(
                post("/api/plan")
                        .with(getJwt("userId"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planJson())
        )
                .andReturn()
                .response

        then:
        response.status == HttpStatus.OK.value()
        1 * planApplicationService.createPlan(USER_ID, LocalDate.of(2022, Month.JULY, 5))
    }

    def "read given plan"() {

        when:
        def response = mvc.perform(
                get("/api/plan")
                        .with(getJwt("userId"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("date", "2022-07-05")
        )
                .andReturn()
                .response

        then:
        response.status == HttpStatus.OK.value()
        1 * planApplicationService.readPlan(USER_ID, LocalDate.of(2022, Month.JULY, 5))
    }

    private static def planJson() {
        """
{
    "date": "2022-07-05"
}
"""
    }

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        PlanApplicationService planApplicationService() {
            detachedMockFactory.Mock(PlanApplicationService)
        }
    }
}
