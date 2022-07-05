package xyz.stasiak.cobudget.plan

import spock.lang.Specification

import java.time.LocalDate
import java.time.Month

class PlanSpec extends Specification {

    static final def CATEGORY_ID = 2

    def "should add plan for category"() {
        given:
        def plan = aPlan()

        when:
        plan.planCategory(CATEGORY_ID, 300)

        then:
        plan.getAmountPlannedForCategory(CATEGORY_ID) == 300
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

    def "should be able to change amount within category"() {
        given:
        def plan = aPlanWithCategoryPlanned()

        when:
        plan.changePlanForCategory(CATEGORY_ID, 100)

        then:
        plan.getAmountPlannedForCategory(CATEGORY_ID) == 100
    }

    def "should plan category when changing not existing"() {
        given:
        def plan = aPlan()

        when:
        plan.changePlanForCategory(CATEGORY_ID, 100)

        then:
        plan.getAmountPlannedForCategory(CATEGORY_ID) == 100
    }

    def aPlan() {
        def plan = new Plan("user", LocalDate.of(2022, Month.JULY, 1))
        plan.setId(1L)
        return plan
    }

    def aPlanWithCategoryPlanned() {
        def plan = new Plan("user", LocalDate.of(2022, Month.JULY, 1))
        plan.setId(1L)
        plan.planCategory(CATEGORY_ID, 300)
        return plan
    }
}
