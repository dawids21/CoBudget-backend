package xyz.stasiak.cobudget.plan


import org.springframework.beans.factory.annotation.Autowired
import xyz.stasiak.cobudget.DataIntegrationSpec

import java.time.LocalDate
import java.time.Month

class PlanApplicationServiceDataIntegrationSpec extends DataIntegrationSpec {

    @Autowired
    private PlanRepository planRepository

    private PlanApplicationService planApplicationService

    void setup() {
        planApplicationService = new PlanApplicationService(planRepository)
    }

    def "return plan for given user"() {
        given:
        aPlan()

        when:
        def plan = planApplicationService.getPlanFor("userId", LocalDate.of(2022, Month.JULY, 5))

        then:
        !plan.isEmpty()
    }

    def "return amount planned for given user"() {
        given:
        def plan = aPlan()

        when:
        planApplicationService.planCategory(plan.getId(), 2, 300)

        then:
        planApplicationService.getAmountPlannedFor(plan.getId(), 2)
    }

    private aPlan() {
        planApplicationService.createPlan("userId", LocalDate.of(2022, Month.JULY, 1))
    }
}
