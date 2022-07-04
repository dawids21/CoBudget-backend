package xyz.stasiak.cobudget.plan

import spock.lang.Specification

import java.time.LocalDate
import java.time.Month

class PlanSpec extends Specification {

    def "should add plan for category"() {
        given:
        def plan = aPlan()

        when:
        plan.planCategory(2L, 300)

        then:
        plan.getAmountPlannedForCategory(2L) == 300
    }

    def "plan should have year and month specified"() {
        given:
        def plan = aPlan()

        expect:
        with(plan.getYearAndMonth()) {
            getYear() == 2022
            getMonth() == Month.JULY
        }
    }

    def aPlan() {
        def plan = new Plan("user", LocalDate.of(2022, Month.JULY, 1))
        plan.setId(1L)
        return plan
    }
}
