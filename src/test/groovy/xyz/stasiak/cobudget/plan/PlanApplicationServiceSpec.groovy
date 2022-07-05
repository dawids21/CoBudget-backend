package xyz.stasiak.cobudget.plan

import io.vavr.control.Option
import spock.lang.Specification
import xyz.stasiak.cobudget.plan.exception.PlanNotFound

import java.time.LocalDate
import java.time.Month

class PlanApplicationServiceSpec extends Specification {

    final def planApplicationService = new PlanApplicationService(new TestPlanRepository())
    final static def CATEGORY_ID = 2

    def "should create plan for new month"() {
        given:
        aPlan()

        expect:
        def plan = planApplicationService.getPlanFor("user", LocalDate.of(2022, Month.JULY, 1))
        !plan.isEmpty()
    }

    def "should return empty option when plan is not created"() {
        expect:
        def plan = planApplicationService.getPlanFor("user", LocalDate.of(2022, Month.JULY, 1))
        plan.isEmpty()
    }

    def "should add plan for given category"() {
        given:
        def plan = aPlan()

        when:
        planApplicationService.planCategory(plan.getId(), CATEGORY_ID, 300)

        then:
        def amount = planApplicationService.getAmountPlannedFor(plan.getId(), CATEGORY_ID)
        amount == 300
    }

    def "should throw an exception when plan does not exists"() {
        when:
        planApplicationService.planCategory(33L, CATEGORY_ID, 300)

        then:
        thrown(PlanNotFound)
    }

    def "should return 0 when category is not planned"() {
        given:
        def plan = aPlan()

        expect:
        planApplicationService.getAmountPlannedFor(plan.getId(), 3) == 0
    }

    def "should be able to change plan for given category"() {
        given:
        def plan = aPlanWithCategoryPlanned()

        when:
        planApplicationService.changeCategoryPlan(plan.getId(), CATEGORY_ID, 100)

        then:
        planApplicationService.getAmountPlannedFor(plan.getId(), CATEGORY_ID) == 100
    }

    private aPlan() {
        planApplicationService.createPlan("user", LocalDate.of(2022, Month.JULY, 1))
    }

    private aPlanWithCategoryPlanned() {
        def plan = planApplicationService.createPlan("user", LocalDate.of(2022, Month.JULY, 1))
        planApplicationService.planCategory(plan.getId(), CATEGORY_ID, 300)
        return plan
    }

    private static class TestPlanRepository implements PlanRepository {

        private final Map<Long, Plan> plans = new HashMap<>()

        @Override
        Plan save(Plan plan) {
            plan.setId(plans.keySet().size() + 1)
            plans.put(plan.getId(), plan)
            return plans.get(plan.getId())
        }

        @Override
        Option<Plan> findById(Long id) {
            return Option.of(plans.get(id))
        }

        @Override
        Option<Plan> findByUserIdAndYearAndMonth(String userId, int year, int month) {
            return Option.ofOptional(
                    plans.values()
                            .stream()
                            .filter(plan -> plan.getUserId() == userId)
                            .filter(plan -> plan.getYearAndMonth().getYear() == year)
                            .filter(plan -> plan.getYearAndMonth().getMonthValue() == month)
                            .findAny()
            )
        }
    }
}
