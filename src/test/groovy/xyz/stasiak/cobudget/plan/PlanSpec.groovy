package xyz.stasiak.cobudget.plan

import spock.lang.Specification

import java.time.LocalDate
import java.time.Month

class PlanSpec extends Specification {

    def "should add plan for category"() {
        given:
        def plan = new Plan("user", LocalDate.of(2022, Month.JULY, 1))
        plan.setId(1L)

        when:
        plan.planCategory(2L, 300)

        then:
        def plannedCategory = plan.getPlanForCategory(2L)
        plannedCategory.getAmount() == 300
    }
}
